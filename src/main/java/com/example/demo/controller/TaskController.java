package com.example.demo.controller;

import com.example.demo.model.TransportTask;
import com.example.demo.model.TaskNode;
import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_doctor')")
    public ResponseEntity<?> createTask(
            @RequestPart("task") TransportTask task,
            @RequestPart("nodes") List<TaskNode> nodes,
            @RequestPart("files") List<MultipartFile> files) {
        return taskService.createTask(task, nodes, files);
    }
}