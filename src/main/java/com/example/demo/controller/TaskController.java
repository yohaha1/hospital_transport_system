package com.example.demo.controller;

import com.example.demo.model.TransportTask;
import com.example.demo.model.TaskNode;
import com.example.demo.response.ApiResponse;
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
        try {
            taskService.createTask(task, nodes, files);
            return ResponseEntity.ok(ApiResponse.success("Task and files created successfully"));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.failure("Invalid input: " + ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ApiResponse.failure("Internal Server Error: " + ex.getMessage()));
        }
    }

    @GetMapping("/searchByStatus")
    @PreAuthorize("hasRole('ROLE_transporter')")
    public ResponseEntity<?> getStatusTasks(@RequestParam("status") String status) {
        try {
            List<TransportTask> pendingTasks = taskService.getStatusTasks(status);
            return ResponseEntity.ok(ApiResponse.success(pendingTasks));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ApiResponse.failure(ex.getMessage()));
        }
    }

    @PostMapping("/accept/{taskId}")
    @PreAuthorize("hasRole('ROLE_transporter')")
    public ResponseEntity<?> acceptTask(@PathVariable("taskId") int taskId,
                                             @RequestParam("transporterId") int transporterId){
        try{
            taskService.acceptTask(taskId, transporterId);
            return ResponseEntity.ok(ApiResponse.success("接单成功！"));
        }catch (IllegalArgumentException ex){
            return ResponseEntity.badRequest().body(ApiResponse.failure("无效输入 " + ex.getMessage()));
        }catch (Exception ex){
            return ResponseEntity.status(500).body(ApiResponse.failure("出错了！" + ex.getMessage()));
        }
    }

    @PostMapping("/start/{taskId}")
    @PreAuthorize("hasRole('ROLE_transporter')")
    public ResponseEntity<?> startTask(@PathVariable("taskId") int taskId,
                                            @RequestParam("transporterId")int transporterId,
                                            @RequestPart("file") MultipartFile file,
                                            @RequestParam("qrCodeData") String qrCodeData){
        try{
            taskService.startTask(taskId,transporterId,file,qrCodeData);
            return ResponseEntity.ok(ApiResponse.success("任务开始！"));
        }catch (IllegalArgumentException ex){
            return ResponseEntity.badRequest().body(ApiResponse.failure("无效输入: " + ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ApiResponse.failure("出错了！" +  ex.getMessage()));
        }
    }

    @PostMapping("/handover/{taskId}")
    @PreAuthorize("hasRole('ROLE_transporter')")
    public ResponseEntity<?> handOverTask(@PathVariable("taskId") int taskId,
                                               @RequestParam("transporterId") int transporterId,
                                               @RequestParam("departmentId") int departmentId,
                                               @RequestPart(value = "file", required = false) MultipartFile file,
                                               @RequestParam("qrCodeData") String qrCodeData){
        try{
            taskService.handOverTask(taskId,transporterId,departmentId,file, qrCodeData);
            return ResponseEntity.ok(ApiResponse.success("任务交接成功！"));
        }catch (IllegalArgumentException ex){
            return ResponseEntity.badRequest().body(ApiResponse.failure("无效输入: " +ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ApiResponse.failure("出错了！" +  ex.getMessage()));
        }
    }

}