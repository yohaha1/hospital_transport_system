package com.example.demo.service.impl;

import com.example.demo.mapper.TaskNodeMapper;
import com.example.demo.mapper.TransportTaskMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.TaskNode;
import com.example.demo.model.TransportTask;
import com.example.demo.service.RecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RecordsServiceImpl implements RecordsService {

    @Autowired
    private TransportTaskMapper transportTaskMapper;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TaskNodeMapper taskNodeMapper;

    @Override
    public List<TransportTask> getAllTaskRecords(String type, Date startDate, Date endDate) {
        return transportTaskMapper.findByFilters(type, startDate, endDate);
    }

    @Override
    public List<TransportTask> getTransporterTaskRecords(int transporterId, String type, Date startDate, Date endDate) {
        return transportTaskMapper.findByTransporterAndFilters(transporterId, type, startDate, endDate);
    }

    @Override
    public List<TransportTask> getDepartmentTaskRecords(int departmentId, String type, Date startDate, Date endDate) {
        List<Integer> doctorIds = userMapper.findDoctorIdsByDepartmentId(departmentId);
        if (doctorIds == null || doctorIds.isEmpty()) {
            // 如果没有医生用户，直接返回空列表
            return List.of();
        }
        return transportTaskMapper.findByDepartmentDoctorIdsAndFilters(doctorIds, type, startDate, endDate);
    }

    @Override
    public List<TaskNode> getTaskNodesByTaskId(int taskId) {
        if(taskId == 0){
            throw new IllegalArgumentException("任务ID不能为空！");
        }
        return taskNodeMapper.selectByTaskId(taskId);
    };

}