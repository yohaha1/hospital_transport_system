package com.example.demo.controller;

import com.example.demo.model.TaskNode;
import com.example.demo.model.TransportTask;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.RecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/records")
public class RecordsController {

    @Autowired
    private RecordsService recordsService;

    //管理员查看所有任务记录
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity<?> getAllTaskRecords(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            List<TransportTask> tasks = recordsService.getAllTaskRecords(type, startDate, endDate);
            return ResponseEntity.ok(ApiResponse.success(tasks));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ApiResponse.failure(ex.getMessage()));
        }
    }

    //运送员查看自己的任务记录
    @GetMapping("/transporter/{transporterId}")
    @PreAuthorize("hasRole('ROLE_transporter')")
    public ResponseEntity<?> getTransporterTaskRecords(
            @PathVariable("transporterId") int transporterId,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            List<TransportTask> tasks = recordsService.getTransporterTaskRecords(transporterId, type, startDate, endDate);
            return ResponseEntity.ok(ApiResponse.success(tasks));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ApiResponse.failure(ex.getMessage()));
        }
    }

    //科室查看所发起的任务
    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasRole('ROLE_doctor')")
    public ResponseEntity<?> getDepartmentTaskRecords(
            @PathVariable("departmentId") int departmentId,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            List<TransportTask> tasks = recordsService.getDepartmentTaskRecords(departmentId, type, startDate, endDate);
            return ResponseEntity.ok(ApiResponse.success(tasks));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ApiResponse.failure(ex.getMessage()));
        }
    }

    //查看某任务的节点序列
    @GetMapping("/taskNodes/{taskId}")
    public ResponseEntity<?> getTaskNodesByTaskId(@PathVariable("taskId") int taskId) {
        try{
            List<TaskNode> nodes = recordsService.getTaskNodesByTaskId(taskId);
            return ResponseEntity.ok(ApiResponse.success(nodes));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.failure(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.failure(e.getMessage()));
        }
    }


}