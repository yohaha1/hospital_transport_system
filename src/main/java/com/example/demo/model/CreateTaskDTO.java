package com.example.demo.model;

import lombok.Data;

import java.util.List;

@Data
public class CreateTaskDTO {
    private TransportTask task;
    private List<TaskNode> nodes;

    public TransportTask getTask() { return task; }
    public void setTask(TransportTask task) { this.task = task; }

    public List<TaskNode> getNodes() { return nodes; }
    public void setNodes(List<TaskNode> nodes) { this.nodes = nodes; }
}