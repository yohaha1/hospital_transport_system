package com.example.demo.controller;

import com.example.demo.model.TransportTask;
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

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity<List<TransportTask>> getAllTaskRecords(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            List<TransportTask> tasks = recordsService.getAllTaskRecords(type, startDate, endDate);
            return ResponseEntity.ok(tasks);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/transporter/{transporterId}")
    @PreAuthorize("hasRole('ROLE_transporter')")
    public ResponseEntity<List<TransportTask>> getTransporterTaskRecords(
            @PathVariable("transporterId") int transporterId,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            List<TransportTask> tasks = recordsService.getTransporterTaskRecords(transporterId, type, startDate, endDate);
            return ResponseEntity.ok(tasks);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasRole('ROLE_doctor')")
    public ResponseEntity<List<TransportTask>> getDepartmentTaskRecords(
            @PathVariable("departmentId") int departmentId,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            List<TransportTask> tasks = recordsService.getDepartmentTaskRecords(departmentId, type, startDate, endDate);
            return ResponseEntity.ok(tasks);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(null);
        }
    }
}