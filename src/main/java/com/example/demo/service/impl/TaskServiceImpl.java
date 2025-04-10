package com.example.demo.service.impl;

import com.example.demo.mapper.TaskNodeMapper;
import com.example.demo.mapper.TransportTaskMapper;
import com.example.demo.model.TaskNode;
import com.example.demo.model.TransportTask;
import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TransportTaskMapper transportTaskMapper;

    @Autowired
    private TaskNodeMapper taskNodeMapper;

    @Override
    public ResponseEntity<String> createTask(TransportTask task, List<TaskNode> nodes) {
        try {
            // 从 JWT token 获取用户ID
            Integer userId = task.getUserid();

            // 设置任务创建时间和用户ID
            task.setCreatetime(new Date());
            task.setUserid(userId);

            transportTaskMapper.insert(task);

            // 设置每个节点的任务ID和顺序
            for (int i = 0; i < nodes.size(); i++) {
                TaskNode node = nodes.get(i);
                node.setTaskid(task.getTaskid());
                node.setSequence(i + 1);
                taskNodeMapper.insert(node);
            }
            return ResponseEntity.ok("Task created successfully");
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}