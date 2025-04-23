package com.example.demo.model;

import lombok.Data;

@Data
public class TaskNodeWithDepartment {
    private TaskNode node;
    private Department department;
}
