package com.example.demo.service;

import com.example.demo.model.TaskNode;
import com.example.demo.model.TransportTask;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface RecordsService {

    List<TransportTask> getAllTaskRecords(String type, Date startDate, Date endDate);

    List<TransportTask> getTransporterTaskRecords(int transporterId, String type, Date startDate, Date endDate);

    List<TransportTask> getDepartmentTaskRecords(int departmentId, String type, Date startDate, Date endDate);

    List<TaskNode> getTaskNodesByTaskId(int taskId);
}