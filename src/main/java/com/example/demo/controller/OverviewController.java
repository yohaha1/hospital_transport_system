package com.example.demo.controller;


import com.example.demo.model.TransportTask;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.OverviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/overview")
public class OverviewController {
    @Autowired
    private OverviewService overviewService;

    //获取正在运输的任务（status = TRANSPORTING）及其节点序列
    @GetMapping("/transportingTasks")
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity<?> getTransportingTasks(){
        try{
            List<TransportTask> transportingTasks= overviewService.getTransportingTasks();
            return ResponseEntity.ok(ApiResponse.success(transportingTasks));
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(ApiResponse.failure(e.getMessage()));
        }catch(Exception e){
            return ResponseEntity.status(500).body(ApiResponse.failure(e.getMessage()));
        }
    }
}
