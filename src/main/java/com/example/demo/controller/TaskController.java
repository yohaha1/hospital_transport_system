package com.example.demo.controller;

import com.example.demo.model.CreateTaskDTO;
import com.example.demo.model.TransportTaskWithDepartmentDTO;
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
    public ResponseEntity<?> createTask(@RequestBody CreateTaskDTO dto) {
        try {
            int taskId = taskService.createTask(dto.getTask(), dto.getNodes());
            return ResponseEntity.ok(ApiResponse.success(taskId));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.failure("Invalid input: " + ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ApiResponse.failure("Internal Server Error: " + ex.getMessage()));
        }
    }

    @PostMapping("/uploadFile")
    @PreAuthorize("hasAnyRole('doctor', 'transporter','admin')")
    public ResponseEntity<?> uploadFile(
            @RequestParam("taskId") int taskId,
            @RequestParam("stage") String stage,
            @RequestPart("file") MultipartFile file) {
        try {
            taskService.saveFileToTask(taskId, file, stage);
            return ResponseEntity.ok(ApiResponse.success("File uploaded successfully"));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.failure("Invalid input: " + ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ApiResponse.failure("Internal Server Error: " + ex.getMessage()));
        }
    }

    @GetMapping("/searchByStatus")
    @PreAuthorize("hasAnyRole('doctor', 'transporter','admin')")
    public ResponseEntity<?> getStatusTasks(@RequestParam("status") String status) {
        try {
            List<TransportTaskWithDepartmentDTO> res = taskService.getStatusTasks(status);
            return ResponseEntity.ok(ApiResponse.success(res));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ApiResponse.failure(ex.getMessage()));
        }
    }

    @PostMapping("/accept/{taskId}")
    @PreAuthorize("hasAnyRole('transporter','admin')")
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
                                            @RequestParam("qrCodeData") String qrCodeData){
        try{
            taskService.startTask(taskId,transporterId,qrCodeData);
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
                                               @RequestParam("qrCodeData") String qrCodeData){
        try{
//            System.out.println("二维码: " + qrCodeData);
            taskService.handOverTask(taskId,transporterId,qrCodeData);
            return ResponseEntity.ok(ApiResponse.success("任务交接成功！"));
        }catch (IllegalArgumentException ex){
            return ResponseEntity.badRequest().body(ApiResponse.failure("无效输入: " +ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ApiResponse.failure("出错了！" +  ex.getMessage()));
        }
    }

    @PostMapping("/handoverConfirm/{taskId}")
    @PreAuthorize("hasRole('ROLE_doctor')")
    public ResponseEntity<?> handOverConfirm(@PathVariable("taskId") int taskId,
                                          @RequestParam("transporterId") int transporterId,
                                          @RequestParam("departmentId") int departmentId){
        try{
            taskService.handOverConfirm(taskId,transporterId,departmentId);
            return ResponseEntity.ok(ApiResponse.success("任务交接成功！"));
        }catch (IllegalArgumentException ex){
            return ResponseEntity.badRequest().body(ApiResponse.failure("无效输入: " +ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ApiResponse.failure("出错了！" +  ex.getMessage()));
        }
    }

    @PostMapping("/cancel/{taskId}")
    @PreAuthorize("hasAnyRole('doctor', 'transporter')")
    public ResponseEntity<?> cancelTask(@PathVariable("taskId") int taskId,
                                        @RequestParam("reason") String reason){
        try{
            taskService.cancelTask(taskId, reason);
            return ResponseEntity.ok(ApiResponse.success(reason));
        }catch (IllegalArgumentException ex){
            return ResponseEntity.badRequest().body(ApiResponse.failure("无效输入: " +ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ApiResponse.failure("出错了！" +  ex.getMessage()));
        }
    }


//    @GetMapping("/getAllTypes")
//    @PreAuthorize("hasAnyRole('doctor','admin')")
//    public ResponseEntity<?> getAllTypes() {
//        try {
//            List<String> res = taskService.getAllTypes();
//            return ResponseEntity.ok(ApiResponse.success(res));
//        } catch (Exception ex) {
//            return ResponseEntity.status(500).body(ApiResponse.failure(ex.getMessage()));
//        }
//    }
}