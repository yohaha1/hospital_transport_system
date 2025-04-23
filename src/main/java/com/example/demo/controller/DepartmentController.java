package com.example.demo.controller;

import com.example.demo.model.Department;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/getAllDepartments")
    @PreAuthorize("hasAnyRole('doctor','admin')")
    public ResponseEntity<?> getAllDepartments() {
        try {
            List<Department> res = departmentService.getAllDepartments();
            return ResponseEntity.ok(ApiResponse.success(res));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ApiResponse.failure(ex.getMessage()));
        }
    }

}
