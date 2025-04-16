package com.example.demo.service;

import com.example.demo.model.TransportTask;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OverviewService {

    List<TransportTask> getTransportingTasks();
}
