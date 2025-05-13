package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.RecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/records")
public class RecordsController {

    @Autowired
    private RecordsService recordsService;

    //管理员查看所有任务记录
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity<?> getAllTaskRecords(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            List<TransportTask> tasks = recordsService.getAllTaskRecords(status, startDate, endDate);
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
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            List<TransportTaskWithDepartmentDTO> tasks = recordsService.getTransporterTaskRecords(transporterId, status, startDate, endDate);
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
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            List<TaskWithTransporterDTO> tasks = recordsService.getDepartmentTaskRecords(departmentId, status, startDate, endDate);
            return ResponseEntity.ok(ApiResponse.success(tasks));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ApiResponse.failure(ex.getMessage()));
        }
    }

    @GetMapping("/departmentHandoverTask/{departmentId}")
    @PreAuthorize("hasRole('ROLE_doctor')")
    public ResponseEntity<?> getdepartmentHandoverTask(
            @PathVariable("departmentId") int departmentId){
        try{
            List<TaskWithTransporterDTO> tasks = recordsService.getDepartmentHandoverTask(departmentId);
            return ResponseEntity.ok(ApiResponse.success(tasks));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ApiResponse.failure(ex.getMessage()));
        }
    }

    //查看某任务的节点序列
    @GetMapping("/taskNodes/{taskId}")
    public ResponseEntity<?> getTaskNodesByTaskId(@PathVariable("taskId") int taskId) {
        try{
            List<TaskNodeWithDepartmentDTO> nodes = recordsService.getTaskNodesByTaskId(taskId);
            return ResponseEntity.ok(ApiResponse.success(nodes));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.failure(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.failure(e.getMessage()));
        }
    }

    @GetMapping("/getFiles/{taskId}")
    public ResponseEntity<?> getFiles(@PathVariable("taskId") int taskId) {
        List<FileInfo> files = recordsService.getFilesByTaskId(taskId);

        return ResponseEntity.ok(ApiResponse.success(files));
    }


}