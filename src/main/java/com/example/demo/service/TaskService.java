package com.example.demo.service;

import com.example.demo.model.TaskNode;
import com.example.demo.model.TransportTask;
import com.example.demo.model.TransportTaskWithDepartmentDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface TaskService{
    int createTask(TransportTask task, List<TaskNode> nodes);

    void saveFileToTask(Long taskId, MultipartFile file);

    void acceptTask(int taskId, int transporterId);

    //开启任务
    void startTask(int taskId, int transporterId, MultipartFile file, String qrCodeData);

    void handOverTask(int taskId, int transporterId, int departmentId, MultipartFile file, String qrCodeData);

    List<TransportTaskWithDepartmentDTO> getStatusTasks(String status);

//    List<String> getAllTypes();
}
