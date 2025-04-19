package com.example.demo.service.impl;

import com.example.demo.mapper.TransportTaskMapper;
import com.example.demo.model.TransportTask;
import com.example.demo.service.OverviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OverviewServiceImpl implements OverviewService {

    @Autowired
    private TransportTaskMapper transportTaskMapper;

    @Override
    public List<TransportTask> getTransportingTasks(){
        return transportTaskMapper.getStatusTasks("TRANSPORTING");
    }

}
