package com.example.demo.service;

import com.example.demo.model.TaskNode;
import com.example.demo.model.TransportTask;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface TaskService{
    void createTask(TransportTask task, List<TaskNode> nodes, List<MultipartFile> files);

    List<TransportTask> getPendingTasks();

    void acceptTask(int taskId, int transporterId);

    void startTask(int taskId, int transporterId, MultipartFile file);
}