package com.example.demo.service;

import com.example.demo.model.TaskNodeWithDepartmentDTO;
import com.example.demo.model.TaskWithTransporterDTO;
import com.example.demo.model.TransportTask;
import com.example.demo.model.TransportTaskWithDepartmentDTO;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface RecordsService {

    List<TransportTask> getAllTaskRecords(String status, Date startDate, Date endDate);

    List<TransportTaskWithDepartmentDTO> getTransporterTaskRecords(int transporterId, String status, Date startDate, Date endDate);

    List<TaskWithTransporterDTO> getDepartmentTaskRecords(int departmentId, String status, Date startDate, Date endDate);

    List<TaskNodeWithDepartmentDTO> getTaskNodesByTaskId(int taskId);
}