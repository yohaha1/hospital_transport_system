package com.example.demo.service;

import com.example.demo.model.TransportTask;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface RecordsService {

    /**
     * 查询所有任务记录 (管理员)
     */
    List<TransportTask> getAllTaskRecords(String type, Date startDate, Date endDate);

    /**
     * 查询运送员任务记录
     */
    List<TransportTask> getTransporterTaskRecords(int transporterId, String type, Date startDate, Date endDate);

    /**
     * 查询科室任务记录
     */
    List<TransportTask> getDepartmentTaskRecords(int departmentId, String type, Date startDate, Date endDate);
}