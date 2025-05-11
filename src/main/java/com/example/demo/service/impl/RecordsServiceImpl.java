package com.example.demo.service.impl;

import com.example.demo.mapper.*;
import com.example.demo.model.*;
import com.example.demo.service.RecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private FileInfoMapper fileInfoMapper;

    @Override
    public List<TransportTask> getAllTaskRecords(String status, Date startDate, Date endDate) {
        return transportTaskMapper.findByFilters(status, startDate, endDate);
    }

    @Override
    public List<TransportTaskWithDepartmentDTO> getTransporterTaskRecords(int transporterId, String status, Date startDate, Date endDate) {
        List<TransportTask> tasks = transportTaskMapper.findByTransporterAndFilters(transporterId, status,startDate, endDate);
        List<TransportTaskWithDepartmentDTO> result = new ArrayList<>();
        for(TransportTask task : tasks) {
            Integer docId = task.getDocid();
            Integer departmentId = userMapper.getDepartmentIdByUserId(docId);
            Department department = null;
            if(departmentId != null) {
                department = departmentMapper.selectByPrimaryKey((long)departmentId);
            }
            TransportTaskWithDepartmentDTO dto = new TransportTaskWithDepartmentDTO();
            dto.setTask(task);
            dto.setDepartment(department);
            result.add(dto);
        }
        return result;
    }

    @Override
    public List<TaskWithTransporterDTO> getDepartmentTaskRecords(int departmentId, String status, Date startDate, Date endDate) {
        List<Integer> doctorIds = userMapper.findDoctorIdsByDepartmentId(departmentId);
        if (doctorIds == null || doctorIds.isEmpty()) {
            // 如果没有医生用户，直接返回空列表
            return List.of();
        }
        List<TransportTask> taskList = transportTaskMapper.findByDepartmentDoctorIdsAndFilters(doctorIds, status, startDate, endDate);

        List<TaskWithTransporterDTO> result = new ArrayList<>();
        for (TransportTask task : taskList) {
            Integer transId = task.getTransid();
            Integer docId = task.getDocid();
            String transName;
            String docName;
            if(transId != null){
                User trans = userMapper.selectByPrimaryKey((long)transId);
                transName = trans.getName();
            }else{
                transName = "";
            }
            if(docId != null){
                User doc = userMapper.selectByPrimaryKey((long)docId);
                docName = doc.getName();
            }else{
                docName = "";
            }
            //  组装返回
            TaskWithTransporterDTO dto = new TaskWithTransporterDTO();
            dto.setTask(task);
            dto.setTransporterName(transName);
            dto.setDoctorName(docName);
            result.add(dto);
        }
//        System.out.println("testttttttttttt"+result);
        return result;
    }

    @Override
    public List<TaskNodeWithDepartmentDTO> getTaskNodesByTaskId(int taskId) {
        if(taskId == 0){
            throw new IllegalArgumentException("任务ID不能为空！");
        }
        List<TaskNode> nodes = taskNodeMapper.selectByTaskId(taskId);
        List<TaskNodeWithDepartmentDTO> result = new ArrayList<>();
        for (TaskNode node : nodes) {
            Integer departmentId = node.getDepartmentid();
            Department department = null;
            if(departmentId != null){
                department = departmentMapper.selectByPrimaryKey(Long.valueOf(departmentId));
            }
            TaskNodeWithDepartmentDTO tnd = new TaskNodeWithDepartmentDTO();
            tnd.setDepartment(department);
            tnd.setNode(node);
            result.add(tnd);
        }
        return result;
    };

    @Override
    public List<FileInfo> getFilesByTaskId(int taskId) {
        return fileInfoMapper.selectByTaskId(taskId);
    }

}