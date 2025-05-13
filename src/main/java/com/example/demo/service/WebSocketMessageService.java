package com.example.demo.service;

import com.example.demo.model.NoticeDTO;
import com.example.demo.util.DepartmentWebSocketHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WebSocketMessageService {

    @Autowired
    private DepartmentWebSocketHandler webSocketHandler;

    public void sendNoticeToDepartment(int departmentId, NoticeDTO notice) throws JsonProcessingException, IOException {
        webSocketHandler.broadcastToDepartment(departmentId, notice);
    }
}