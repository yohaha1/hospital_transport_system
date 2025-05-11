package com.example.demo.controller;

import com.example.demo.model.NoticeDTO;
import com.example.demo.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;

    @MessageMapping("/handover/confirm")
    public boolean confirmHandover(@Payload NoticeDTO notice) {
        // 调用服务层处理确认
        messageService.handOverConfirm(notice.getTaskId());
        return true;
    }
}
