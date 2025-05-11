package com.example.demo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DepartmentWebSocketHandler extends TextWebSocketHandler {

    private final Map<Integer, List<WebSocketSession>> departmentSessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();
        JSONObject json = new JSONObject(payload);

        if ("subscribe".equals(json.getString("type"))) {
            try {
                // 使用 getInt 获取整数类型的 departmentId
                int departmentId = json.getInt("departmentId");
                if (!departmentSessions.containsKey(departmentId)) {
                    departmentSessions.put(departmentId, new ArrayList<>());
                }
                departmentSessions.get(departmentId).add(session);
            } catch (Exception e) {
                // 处理非法输入（如非整数的 departmentId）
                System.err.println("Invalid departmentId format: " + e.getMessage());
            }
        }
    }

    public void broadcastToDepartment(int departmentId, Object notice) throws JsonProcessingException, IOException {
        String jsonMessage = objectMapper.writeValueAsString(notice);
        List<WebSocketSession> sessions = departmentSessions.get(departmentId);
        if (sessions != null) {
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(jsonMessage));
                }
            }
        }
    }
}