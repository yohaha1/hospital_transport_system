package com.example.demo.service.impl;

import com.example.demo.mapper.FileInfoMapper;
import com.example.demo.mapper.TaskNodeMapper;
import com.example.demo.mapper.TransportTaskMapper;
import com.example.demo.model.FileInfo;
import com.example.demo.model.TaskNode;
import com.example.demo.model.TransportTask;
import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File ;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TransportTaskMapper transportTaskMapper;

    @Autowired
    private TaskNodeMapper taskNodeMapper;

    @Autowired
    private FileInfoMapper fileMapper;

    @Override
    public void createTask(TransportTask task, List<TaskNode> nodes, List<MultipartFile> files) {
        // 设置任务创建时间
        task.setCreatetime(new Date());
        transportTaskMapper.insert(task);

        // 设置每个节点的任务ID和顺序
        if (nodes == null || nodes.isEmpty()) {
            throw new IllegalArgumentException("Nodes list cannot be null or empty");
        }
        for (int i = 0; i < nodes.size(); i++) {
            TaskNode node = nodes.get(i);
            node.setTaskid(task.getTaskid());
            node.setSequence(i + 1);
            taskNodeMapper.insert(node);
        }

        // 保存附件
        if (files != null && !files.isEmpty()) {
            String uploadDir = "D:/project/graduate_project/data/" + task.getTaskid();
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }

            for (MultipartFile file : files) {
                String originalFilename = file.getOriginalFilename();
                String filePath = uploadDir + "/" + originalFilename;

                File dest = new File(filePath);
                try {
                    file.transferTo(dest);
                } catch (IOException e) {
                    throw new RuntimeException("文件上传失败: " + originalFilename, e);
                }

                FileInfo fileInfo = new FileInfo();
                fileInfo.setTaskid(task.getTaskid());
                fileInfo.setFilepath(filePath);
                fileInfo.setFilename(originalFilename);
                fileInfo.setFiletype(file.getContentType());
                fileInfo.setStage("CREATION");
                fileInfo.setUploadtime(new Date());
                fileMapper.insert(fileInfo);
            }
        }
    }

    @Override
    public List<TransportTask> getPendingTasks() {
        return transportTaskMapper.getPendingTasks();
    }

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
        task.setStatus("ACCEPTED");
        int res = transportTaskMapper.updateByPrimaryKeySelective(task);
        if (res != 1) {
            throw new RuntimeException("更新任务状态失败！");
        }
    }

    @Override
    public void startTask(int taskId, int transporterId, MultipartFile file) {
        TransportTask task = transportTaskMapper.selectByPrimaryKey((long) taskId);
        if (task == null) {
            throw new IllegalArgumentException("任务不存在！");
        }
        if (!"ACCEPTED".equals(task.getStatus())) {
            throw new IllegalArgumentException("任务状态异常，无法开始！");
        }
        if(transporterId != task.getTransid()){
            throw new IllegalArgumentException("运送员信息异常！");
        }

        // 处理上传图片
        String uploadDir = "D:/project/graduate_project/data/" + taskId;
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }

        String originalFilename = file.getOriginalFilename();
        String filePath = uploadDir + originalFilename;

        try {
            // 保存文件到指定路径
            File dest = new File(filePath);
            file.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败！", e);
        }

        // 插入文件信息到数据库
        FileInfo fileInfo = new FileInfo();
        fileInfo.setTaskid(taskId);
        fileInfo.setFilepath(filePath);
        fileInfo.setFilename(originalFilename);
        fileInfo.setFiletype(file.getContentType());
        fileInfo.setStage("PICKUP");
        fileInfo.setUploadtime(new Date());
        fileMapper.insert(fileInfo);

        // 更新任务节点表，记录时间
        TaskNode startNode = taskNodeMapper.selectByTaskIdAndSequence(taskId, 1); // 假设第一个节点为任务开始节点
        if (startNode == null) {
            throw new RuntimeException("任务节点不存在！");
        }
        startNode.setHandovertime(new Date());
        int nodeUpdateResult = taskNodeMapper.updateByPrimaryKeySelective(startNode);
        if (nodeUpdateResult != 1) {
            throw new RuntimeException("更新任务节点时间失败！");
        }

        task.setStatus("TRANSPORTING");
        int res = transportTaskMapper.updateByPrimaryKeySelective(task);
        if (res != 1) {
            throw new RuntimeException("更新任务状态失败！");
        }
    }

}