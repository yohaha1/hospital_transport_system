package com.example.demo.service;

import com.example.demo.model.TaskNode;
import com.example.demo.model.TransportTask;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface TaskService{
    ResponseEntity<String> createTask(TransportTask task, List<TaskNode> nodes, String token);

}