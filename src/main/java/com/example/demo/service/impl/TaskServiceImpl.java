package com.example.demo.service.impl;

import com.example.demo.mapper.FileInfoMapper;
import com.example.demo.mapper.TaskNodeMapper;
import com.example.demo.mapper.TransportTaskMapper;
import com.example.demo.model.FileInfo;
import com.example.demo.model.TaskNode;
import com.example.demo.model.TransportTask;
import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> createTask(TransportTask task, List<TaskNode> nodes, List<MultipartFile> files) {
        try {
            // 从 JWT token 获取用户ID
            Integer userId = task.getUserid();

            // 设置任务创建时间和用户ID
            task.setCreatetime(new Date());
            task.setUserid(userId);
            transportTaskMapper.insert(task);
            System.out.println("创建任务成功： " + task.getTaskid());

            // 设置每个节点的任务ID和顺序
            if (nodes != null && !nodes.isEmpty()) {
                for (int i = 0; i < nodes.size(); i++) {
                    TaskNode node = nodes.get(i);

                    // 设置任务ID和节点顺序
                    node.setTaskid(task.getTaskid());
                    node.setSequence(i + 1);

                    // 插入节点数据到数据库
                    taskNodeMapper.insert(node);
                }
            } else {
                throw new IllegalArgumentException("Nodes list cannot be null or empty");
            }

            //保存附件
            String uploadDir = "resources/tasks/" + task.getTaskid() ;
            File uploadDirFile = new File(uploadDir);
            if(!uploadDirFile.exists()){
                uploadDirFile.mkdirs();
            }

            for(MultipartFile file : files){
                String originalFilename = file.getOriginalFilename();
                String filePath = uploadDir + "/" + originalFilename;

                //将文件保存
                File dest  = new File(filePath);
                file.transferTo(dest);

                //插入文件信息到数据
                FileInfo fileInfo = new FileInfo();
                fileInfo.setTaskid(task.getTaskid());
                fileInfo.setFilepath(filePath);
                fileInfo.setFilename(originalFilename);
                fileInfo.setFiletype(file.getContentType());
                fileInfo.setStage("CREATION");
                fileInfo.setUploadtime(new Date());
                fileMapper.insert(fileInfo);
            }

            return ResponseEntity.ok("Task and files created successfully");
        } catch (IOException ex) {
            System.out.println("没有文件");
            return ResponseEntity.status(200).body("File upload failed");
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}