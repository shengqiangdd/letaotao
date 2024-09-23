package com.gxcy.letaotao.web.app.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gxcy.letaotao.common.enums.BooleanStatus;
import com.gxcy.letaotao.web.app.service.WeChatUserService;
import com.gxcy.letaotao.web.app.vo.LTUserVo;
import com.gxcy.letaotao.web.app.vo.LTWechatMessageVo;
import com.gxcy.letaotao.web.app.vo.MessageResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通知推送
 */
@Component
@Slf4j
public class NotificationWebSocketHandler extends TextWebSocketHandler {

    private static final String USER_ID = "userId";

    @Resource
    private WeChatUserService userService;
    @Resource
    private ObjectMapper objectMapper;
    // 使用 ConcurrentHashMap 存储每个用户编号对应的 WebSocketSession
    private final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 解析用户编号
        String query = Objects.requireNonNull(session.getUri()).getQuery();
        String[] queryParams = query.split("&");

        Long userId = null;

        for (String param : queryParams) {
            String[] keyValue = param.split("=");
            if (USER_ID.equals(keyValue[0])) {
                userId = Long.valueOf(keyValue[1]);
            }
        }

        String sessionId = session.getId();
        session.getAttributes().put(USER_ID, userId);
        // 将 WebSocketSession 添加到 Map 中
        sessions.put(userId, session);

        log.debug("WebSocket connection established for userId: {}, sessionId: {}", userId, sessionId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Long userId = (Long) session.getAttributes().get(USER_ID);
        sessions.remove(userId);

        log.debug("WebSocket connection closed for userId: {}, status: {}", userId, status);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

    }

    public void sendNotification(Long receiverId, MessageResponse messageResponse) {
        // 获取接收者的 WebSocketSession
        WebSocketSession session = sessions.get(receiverId);
        if (session != null && session.isOpen()) {
            try {
                LTWechatMessageVo message = messageResponse.getMessage();
                LTUserVo sender = userService.findUserById(message.getSenderId());
                message.setNickName(sender.getNickName());
                message.setAvatar(sender.getAvatar());
                if (message.getIsImage() == BooleanStatus.TRUE) {
                    message.setContent("您有一条新消息，请注意查看！");
                }
                // 发送消息
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(messageResponse)));
            } catch (IOException e) {
                log.error("Error sending notification to user: {}", receiverId, e);
            }
        } else {
            log.warn("No active WebSocket session found for userId: {}", receiverId);
        }
    }

}
