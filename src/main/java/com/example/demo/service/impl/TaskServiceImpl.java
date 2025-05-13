package com.example.demo.service.impl;

import com.example.demo.mapper.*;
import com.example.demo.model.*;
import com.example.demo.service.WebSocketMessageService;
import com.example.demo.util.QRCodeValidator;
import com.example.demo.service.TaskService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File ;
import java.io.IOException;
import java.util.*;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TransportTaskMapper transportTaskMapper;

    @Autowired
    private TaskNodeMapper taskNodeMapper;

    @Autowired
    private FileInfoMapper fileMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private ReceivedNotificationMapper receivedNotificationMapper;

    @Autowired
    private WebSocketMessageService webSocketMessageService;

    // 创建任务（不带文件）
    @Override
    public int createTask(TransportTask task, List<TaskNode> nodes) {
        task.setCreatetime(new Date());
        transportTaskMapper.insert(task);

        if (nodes == null || nodes.isEmpty()) {
            throw new IllegalArgumentException("Nodes list cannot be null or empty");
        }
        for (int i = 0; i < nodes.size(); i++) {
            TaskNode node = nodes.get(i);
            node.setTaskid(task.getTaskid());
            node.setSequence(i + 1);
            taskNodeMapper.insert(node);
        }

        return task.getTaskid();
    }

    // 上传文件到任务
    @Override
    public void saveFileToTask(int taskId, MultipartFile file, String stage) {
        if (file == null) {
            throw new IllegalArgumentException("未上传图片！");
        }
        // 文件保存路径
        String uploadDir = "D:/project/graduate_project/data/" + taskId;
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }

        String originalFilename = file.getOriginalFilename();
        String filePath = uploadDir + "/" + originalFilename;

        try {
            File dest = new File(filePath);
            file.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败: " + originalFilename, e);
        }

        FileInfo fileInfo = new FileInfo();
        fileInfo.setTaskid(taskId);
        fileInfo.setFilepath(filePath);
        fileInfo.setFilename(originalFilename);
        fileInfo.setFiletype(file.getContentType());
        fileInfo.setStage(stage);
        fileInfo.setUploadtime(new Date());
        fileMapper.insert(fileInfo);
    }

    @Override
    public List<TransportTaskWithDepartmentDTO> getStatusTasks(String status) {
        List<TransportTask> taskList = transportTaskMapper.getStatusTasks(status);
        // 定义排序优先级
        Map<String, Integer> statusOrder = new HashMap<>();
        statusOrder.put("NEW", 0);
        statusOrder.put("TRANSPORTING", 1);
        statusOrder.put("DELIVERED", 2);

        // 排序
        taskList.sort(Comparator.comparingInt(
                t -> statusOrder.getOrDefault(t.getStatus(), Integer.MAX_VALUE)
        ));

        List<TransportTaskWithDepartmentDTO> result = new ArrayList<>();
        for (TransportTask task : taskList) {
            Integer docId = task.getDocid();
            // 1. 通过docid查doctor表获得departmentId
            Integer departmentId = userMapper.getDepartmentIdByUserId(docId);
            Department department = null;
            if (departmentId != null) {
                department = departmentMapper.selectByPrimaryKey(Long.valueOf(departmentId));
            }
            // 2. 组装返回
            TransportTaskWithDepartmentDTO dto = new TransportTaskWithDepartmentDTO();
            dto.setTask(task);
            dto.setDepartment(department);
            result.add(dto);
        }
        return result;
    }

    //运送员接单
    @Override
    @Transactional(
            rollbackFor = {Exception.class},
            propagation = Propagation.REQUIRES_NEW,
            isolation = Isolation.REPEATABLE_READ
    )
    public void acceptTask(int taskId, int transporterId) {
        TransportTask task = transportTaskMapper.selectByPrimaryKey((long) taskId);
        if(task == null) {
            throw new IllegalArgumentException("没有找到任务: " + taskId);
        }
        if(!"NEW".equals(task.getStatus())) {
            throw new IllegalArgumentException("任务状态异常，已被接单或已取消，无法接单");
        }
        //更新任务状态，设置运送员
        task.setTransid(transporterId);
        task.setStatus("TRANSPORTING");
        int res = transportTaskMapper.updateByPrimaryKeySelective(task);
        if (res != 1) {
            throw new RuntimeException("更新任务状态失败！");
        }
    }

    //开启任务
    @Override
    public void startTask(int taskId, int transporterId, String qrCodeData) {
        stateCheck(taskId,transporterId,"TRANSPORTING");

        // 初始化二维码校验器
        QRCodeValidator qrCodeValidator = new QRCodeValidator(departmentMapper);

        //获取起始节点
        TaskNode startNode = taskNodeMapper.selectByTaskIdAndSequence(taskId, 1); // 第一个节点为任务开始节点
        if (startNode == null) {
            throw new RuntimeException("任务起始节点不存在！");
        }

        // 校验二维码
        qrCodeValidator.validateQRCode(qrCodeData, startNode.getDepartmentid(), 600); // 600秒过期

        // 更新任务节点表，记录时间
        startNode.setHandovertime(new Date());
        int nodeUpdateResult = taskNodeMapper.updateByPrimaryKeySelective(startNode);
        if (nodeUpdateResult != 1) {
            throw new RuntimeException("更新任务节点时间失败！");
        }

    }

    //任务交接
    @Override
    public void handOverTask(int taskId, int transporterId, String qrCodeData) {
        stateCheck(taskId,transporterId,"TRANSPORTING");

        //获取改任务的所有节点
        List<TaskNode> taskNodes = taskNodeMapper.selectByTaskId(taskId);

        // 找到已交接的最大序号
        int maxCompletedSequence = taskNodes.stream()
                .filter(node -> node.getHandovertime() != null)
                .mapToInt(TaskNode::getSequence)
                .max()
                .orElse(0);

        // 获取下一个期望交接的节点
        TaskNode correctNode = taskNodes.stream()
                .filter(node -> node.getSequence() == maxCompletedSequence + 1)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("未找到下一个交接节点！"));

        // 获取期望交接的部门
        Department expectedDepartment = departmentMapper.selectByPrimaryKey((long) correctNode.getDepartmentid());
        if (expectedDepartment == null) {
            throw new IllegalArgumentException("期望交接的部门不存在！");
        }

        // 初始化二维码校验器
        QRCodeValidator qrCodeValidator = new QRCodeValidator(departmentMapper);
        // 校验二维码
        qrCodeValidator.validateQRCode(qrCodeData, expectedDepartment.getDepartmentid(), 120); // 120秒过期

        User trans = userMapper.selectByPrimaryKey((long) transporterId);
        TransportTask task = transportTaskMapper.selectByPrimaryKey((long) taskId);

        NoticeDTO notice = new NoticeDTO();
        notice.setTaskId(taskId);
        notice.setItemName(task.getItemname());
        notice.setDepartmentId(correctNode.getDepartmentid());
        notice.setTransName(trans.getName());
        notice.setType("handoverConfirm");

        try {
            webSocketMessageService.sendNoticeToDepartment(correctNode.getDepartmentid(), notice);
        } catch (JsonProcessingException e) {
            // 处理 JSON 序列化异常（如 NoticeDTO 无法被 Jackson 序列化）
            System.err.println("JSON serialization error: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            // 处理 WebSocket 发送异常（如连接关闭）
            System.err.println("WebSocket send error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void handOverConfirm(int taskId, int transporterId , int departmentId) {
        stateCheck(taskId,transporterId,"TRANSPORTING");
        // 1. 查询并找到下一个节点
        List<TaskNode> nodes = taskNodeMapper.selectByTaskId(taskId);
        int maxCompletedSeq = nodes.stream()
                .filter(n -> n.getHandovertime() != null)
                .mapToInt(TaskNode::getSequence)
                .max().orElse(0);
        TaskNode nextNode = nodes.stream()
                .filter(n -> n.getSequence() == maxCompletedSeq + 1)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("无待处理节点"));

        // 2. 更新节点交接时间
        nextNode.setHandovertime(new Date());
        int updateCount = taskNodeMapper.updateByPrimaryKeySelective(nextNode);
        if (updateCount != 1) {
            throw new RuntimeException("更新任务节点交接时间失败！");
        }

        // 3. 如果是最后一个节点，则更新任务状态
        int maxSeqAll = nodes.stream().mapToInt(TaskNode::getSequence).max().orElse(0);
        if (nextNode.getSequence() == maxSeqAll) {
            TransportTask task = transportTaskMapper.selectByPrimaryKey((long) taskId);
            task.setStatus("DELIVERED");
            task.setCompletion(new Date());
            int taskUpdate = transportTaskMapper.updateByPrimaryKeySelective(task);
            if (taskUpdate != 1) {
                throw new RuntimeException("更新任务状态为 DELIVERED 失败！");
            }
        }

        NoticeDTO notice = new NoticeDTO();
        notice.setType("handoverConfirmAck");

        try {
            webSocketMessageService.sendNoticeToDepartment(departmentId, notice);
        } catch (JsonProcessingException e) {
            System.err.println("JSON 序列化异常: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("WebSocket 发送失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void cancelTask(int taskId, String reason) {
        TransportTask task = transportTaskMapper.selectByPrimaryKey((long) taskId);

        task.setStatus("CANCELED");
        transportTaskMapper.updateByPrimaryKeySelective(task);

        //新建通知
        Notification notification = new Notification();
        notification.setTaskid(taskId);
        notification.setNotificationtype("TASK_CANCELLED");
        notification.setMessage(String.format("任务已被取消，原因：%s", reason));
        notification.setSendtime(new Date());
        notificationMapper.insert(notification);

        //通知医生和运送员
        int docId = task.getDocid();
        ReceivedNotification docNotification = new ReceivedNotification();
        docNotification.setNotificationid(notification.getNotificationid());
        docNotification.setUserid(docId);
        receivedNotificationMapper.insert(docNotification);

        if (task.getTransid() != null) {
            int transId = task.getTransid();
            ReceivedNotification transNotification = new ReceivedNotification();
            transNotification.setNotificationid(notification.getNotificationid());
            transNotification.setUserid(transId);
            receivedNotificationMapper.insert(transNotification);
        }

        System.out.println("任务取消: " + taskId);
    }

//    @Override
//    public List<String> getAllTypes() {
//        return transportTaskMapper.getAllTypes();
//    }

    //检查任务状态是否正确，以及运送员信息等
    private void stateCheck(int taskId, int transporterId, String state) {
        TransportTask task = transportTaskMapper.selectByPrimaryKey((long) taskId);
        if (task == null) {
            throw new IllegalArgumentException("任务不存在！");
        }
        if (!state.equals(task.getStatus())) {
            throw new IllegalArgumentException("任务处于 "+task.getStatus() +" 状态，无法操作！");
        }
        if(transporterId != task.getTransid()){
            throw new IllegalArgumentException("运送员信息异常！");
        }
    }

}