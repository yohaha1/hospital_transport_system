package com.example.demo.service.impl;

import com.example.demo.mapper.*;
import com.example.demo.model.*;
import com.example.demo.util.QRCodeValidator;
import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        stateCheck(taskId,transporterId,"ACCEPTED");

        // 初始化二维码校验器
        QRCodeValidator qrCodeValidator = new QRCodeValidator(departmentMapper);

        //获取起始节点
        TaskNode startNode = taskNodeMapper.selectByTaskIdAndSequence(taskId, 1); // 第一个节点为任务开始节点
        if (startNode == null) {
            throw new RuntimeException("任务起始节点不存在！");
        }

        // 校验二维码
        qrCodeValidator.validateQRCode(qrCodeData, startNode.getDepartmentid(), 120); // 120秒过期

        // 更新任务节点表，记录时间
        startNode.setHandovertime(new Date());
        int nodeUpdateResult = taskNodeMapper.updateByPrimaryKeySelective(startNode);
        if (nodeUpdateResult != 1) {
            throw new RuntimeException("更新任务节点时间失败！");
        }

        //更新任务状态
        TransportTask task = transportTaskMapper.selectByPrimaryKey((long) taskId);
        task.setStatus("TRANSPORTING");
        int res = transportTaskMapper.updateByPrimaryKeySelective(task);
        if (res != 1) {
            throw new RuntimeException("更新任务状态失败！");
        }
    }

    //任务交接
    @Override
    public void handOverTask(int taskId, int transporterId, String qrCodeData) {
        stateCheck(taskId,transporterId,"TRANSPORTING");

//        TaskNode currentNode = taskNodeMapper.selectByTaskIdAndDepartmentId(taskId, departmentId);
//        if(currentNode == null) {
//            throw new IllegalArgumentException("目标部门的任务节点不存在！");
//        }
//        if (currentNode.getHandovertime() != null) {
//            throw new IllegalArgumentException("目标节点已完成交接，无法重复交接！");
//        }

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

        //更新当前节点的交接时间
        correctNode.setHandovertime(new Date());
        int res = taskNodeMapper.updateByPrimaryKeySelective(correctNode);
        if (res != 1) {
            throw new RuntimeException("更新任务节点交接时间失败！");
        }

        // 检查是否是最后一个节点
        int maxSequence = taskNodes.stream()
                .mapToInt(TaskNode::getSequence)
                .max()
                .orElseThrow(() -> new IllegalArgumentException("无法获取任务的最大节点序号！"));

        if (correctNode.getSequence() == maxSequence) {
            // 如果是最后一个节点，更新任务状态为 "DELIVERED"
            TransportTask task = transportTaskMapper.selectByPrimaryKey((long) taskId);
            task.setStatus("DELIVERED");
            task.setCompletion(new Date());
            int taskRes = transportTaskMapper.updateByPrimaryKeySelective(task);
            if (taskRes != 1) {
                throw new RuntimeException("更新任务状态为 DELIVERED 失败！");
            }
        }
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