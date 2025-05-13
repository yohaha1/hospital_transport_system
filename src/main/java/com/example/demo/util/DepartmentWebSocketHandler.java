package com.example.demo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.model.NoticeDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class DepartmentWebSocketHandler extends TextWebSocketHandler {

    private final ConcurrentHashMap<Integer, CopyOnWriteArrayList<WebSocketSession>> departmentSessions = new ConcurrentHashMap<>();
    // 离线缓存消息
    private final ConcurrentHashMap<Integer, CopyOnWriteArrayList<TextMessage>> offlineCache = new ConcurrentHashMap<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

//    在客户端连接建立后调用
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // 打印连接建立事件
        System.out.println("[连接建立][ sessionId  " + session.getId() + "] ");
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        // 打印接收的原始消息
        System.out.println("接受消息：" + session.getId() + "] " + payload);

        try {
            JsonNode json = objectMapper.readTree(payload);
            String type = json.path("type").asText();
            if ("subscribe".equals(type)) {
                int dept = json.path("departmentId").asInt();
                departmentSessions
                        .computeIfAbsent(dept, d -> new CopyOnWriteArrayList<>())
                        .add(session);

                // 回放离线缓存
                System.out.println("缓存内容：  "+ offlineCache);
                List<TextMessage> cacheList = offlineCache.remove(dept);
                if (cacheList != null) {
                    for (TextMessage cached : cacheList) {
                        try {
                            session.sendMessage(cached);
                            System.out.println("[回放缓存] [" + session.getId() + "] " + cached.getPayload());
                        } catch (IOException e) {
                            System.out.println("[回放错误 ][" + session.getId() + "] " + e.getMessage());
                        }
                    }
                }
            } else {
                System.out.println("[类型错误 ][" + session.getId() + "] type=" + type);
            }
        } catch (IOException e) {
            System.out.println("[PARSE-错误][" + session.getId() + "] " + e.getMessage());
        }
    }

    public void broadcastToDepartment(int departmentId, NoticeDTO notice) {
        String jsonStr;
        try {
            jsonStr = objectMapper.writeValueAsString(notice);
        } catch (JsonProcessingException e) {
            System.out.println("[序列化错误 ] " + e.getMessage());
            return;
        }
        TextMessage msg = new TextMessage(jsonStr);

        CopyOnWriteArrayList<WebSocketSession> sessions = departmentSessions.get(departmentId);
        if (sessions == null || sessions.isEmpty()) {
            // 离线缓存
            offlineCache
                    .computeIfAbsent(departmentId, d -> new CopyOnWriteArrayList<>())
                    .add(msg);
            System.out.println("[缓存消息 ][" + departmentId + "] " + jsonStr);
            System.out.println("之后缓存内容：  "+ offlineCache);

            return;
        }

        // 在线推送
        sessions.removeIf(s -> !s.isOpen());
        for (WebSocketSession sess : sessions) {
            try {
                sess.sendMessage(msg);
                System.out.println("[发送消息][" + sess.getId() + "] 到部门 =" + departmentId + " 内容=" + jsonStr);
            } catch (IOException e) {
                System.out.println("[发送错误 ][" + sess.getId() + "] " + e.getMessage());
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        departmentSessions.values().forEach(list -> list.remove(session));
        System.out.println("[连接关闭][sessionId:" + session.getId() + "] status=" + status);
    }
}
