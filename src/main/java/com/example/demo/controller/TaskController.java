package com.example.demo.controller;

import com.example.demo.model.TransportTask;
import com.example.demo.model.TaskNode;
import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> createTask(@RequestBody TransportTask task, @RequestBody List<TaskNode> nodes, @RequestHeader("Authorization") String token) {
        try {
            taskService.createTask(task, nodes, token);
            return ResponseEntity.ok("Task created successfully");
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}