package com.example.demo.service;

import com.example.demo.model.TaskNode;
import com.example.demo.model.TransportTask;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface TaskService{
    void createTask(TransportTask task, List<TaskNode> nodes, List<MultipartFile> files);

    void acceptTask(int taskId, int transporterId);

    //开启任务
    void startTask(int taskId, int transporterId, MultipartFile file, String qrCodeData);

    void handOverTask(int taskId, int transporterId, int departmentId, MultipartFile file, String qrCodeData);

    List<TransportTask> getStatusTasks(String status);
}