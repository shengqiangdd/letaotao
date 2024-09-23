package com.gxcy.letaotao.web.app.config.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gxcy.letaotao.common.entity.LTOrder;
import com.gxcy.letaotao.common.enums.*;
import com.gxcy.letaotao.web.app.service.LTWeChatMessageService;
import com.gxcy.letaotao.web.app.service.LTWeChatOrderService;
import com.gxcy.letaotao.web.app.vo.LTWechatMessageVo;
import com.gxcy.letaotao.web.app.vo.MessageResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * WebSocket处理器
 */
@Component
@Slf4j
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final String CHAT_ID = "chatId";
    private static final String MESSAGE_TYPE = "messageType";
    private static final String ORDER_ID = "orderId";
    private static final String NO_READ_IDS = "noReadIds";
    private static final String NEW_STATUS = "newStatus";
    private static final String SENDER_ID = "senderId";
    private static final String RELATION_ID = "relationId";
    private static final String RECEIVER_ID = "receiverId";
    private static final String IS_IMAGE = "isImage";
    private static final String CONTENT = "content";

    @Resource
    private LTWeChatMessageService ltWeChatMessageService;
    @Resource
    private LTWeChatOrderService ltWeChatOrderService;
    @Resource
    private NotificationWebSocketHandler notificationWebSocketHandler;
    @Resource
    private ObjectMapper objectMapper;

    // 使用 ConcurrentHashMap 存储每个 chatId 对应的 WebSocketSession 列表
    private final Map<Integer, List<WebSocketSession>> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 解析查询字符串以获取 chatId
        String query = Objects.requireNonNull(session.getUri()).getQuery();
        String[] queryParams = query.split("&");

        Integer chatId = null;

        for (String param : queryParams) {
            String[] keyValue = param.split("=");
            if (CHAT_ID.equals(keyValue[0])) {
                chatId = Integer.valueOf(keyValue[1]);
            }
        }

        String sessionId = session.getId();
        // 将 chatId 存储在 session 的属性中
        session.getAttributes().put(CHAT_ID, chatId);

        // 初始化存储当前 chatId 的 session 列表
        sessions.putIfAbsent(chatId, new ArrayList<>());
        // 将当前 session 添加到 chatId 对应的 session 列表中
        sessions.get(chatId).add(session);

        log.debug("WebSocket connection established for chatId: {}, sessionId: {}", chatId, sessionId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Integer chatId = (Integer) session.getAttributes().get(CHAT_ID);

        // 从 session 列表中移除当前 session
        List<WebSocketSession> chatSessions = sessions.get(chatId);
        if (chatSessions != null) {
            chatSessions.remove(session);
            if (chatSessions.isEmpty()) {
                sessions.remove(chatId);
            }
        }

        log.debug("WebSocket connection closed for chatId: {}, status: {}", chatId, status);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        // 将JSON文本消息转换为Map
        Map<String, Object> messageMap = objectMapper.readValue(message.getPayload(),
                new TypeReference<Map<String, Object>>() {
                });

        String messageType = (String) messageMap.get(MESSAGE_TYPE);
        if (!ObjectUtils.isEmpty(messageType)) {
            // 根据消息类型解析出枚举值
            LTChatType ltChatType = LTChatType.valueOf(messageType.toUpperCase());
            messageMap.put(MESSAGE_TYPE, ltChatType);
            switch (ltChatType) {
                case CHAT:
                    // 处理聊天消息
                    handleChatMessage(session, messageMap);
                    break;
                case READ:
                    // 处理未读消息
                    handleChatMessageRead(session, messageMap);
                    break;
                case ORDER:
                    // 处理订单状态更新
                    handleOrderStatusUpdateMessage(session, messageMap);
                    break;
                default:
                    break;
            }
        }
    }

    // 处理订单状态更新消息
    private void handleOrderStatusUpdateMessage(WebSocketSession session, Map<String, Object> messageMap) throws IOException {
        Integer chatId = (Integer) session.getAttributes().get(CHAT_ID);
        // 从messageMap解析出订单ID和状态
        Integer orderId = (Integer) messageMap.get(ORDER_ID);
        LTOrderMessageStatus newStatus = (LTOrderMessageStatus) messageMap.get(NEW_STATUS);
        LTWechatMessageVo ltMessage = new LTWechatMessageVo();
        ltMessage.setIsOrder(BooleanStatus.TRUE);
        ltMessage.setOrderId(orderId);
        // 根据订单ID获取订单信息
        LTOrder ltOrder = ltWeChatOrderService.getById(orderId);
        // 根据订单状态获取对应的消息标题和内容
        switch (newStatus) {
            case AlreadyBuy:
                ltMessage.setTitle(LTOrderMessageStatus.AlreadyBuy.getTitle());
                ltMessage.setContent(LTOrderMessageStatus.AlreadyBuy.getContent());
                break;
            case AlreadyPay:
                ltMessage.setTitle(LTOrderMessageStatus.AlreadyPay.getTitle());
                ltMessage.setContent(LTOrderMessageStatus.AlreadyPay.getContent());
                break;
            case AlreadyShip:
                ltMessage.setTitle(LTOrderMessageStatus.AlreadyShip.getTitle());
                ltMessage.setContent(LTOrderMessageStatus.AlreadyShip.getContent());
                break;
            case AlreadyFinish:
                ltMessage.setTitle(LTOrderMessageStatus.AlreadyFinish.getTitle());
                ltMessage.setContent(LTOrderMessageStatus.AlreadyFinish.getContent());
                break;
            case AlreadyCancel:
                ltMessage.setTitle(LTOrderMessageStatus.AlreadyCancel.getTitle());
                ltMessage.setContent(LTOrderMessageStatus.AlreadyCancel.getContent());
                break;
            case AlreadyEvaluate:
                ltMessage.setTitle(LTOrderMessageStatus.AlreadyEvaluate.getTitle());
                ltMessage.setContent(LTOrderMessageStatus.AlreadyEvaluate.getContent());
                break;
            default:
                break;
        }
        ltMessage.setSenderId(((Integer) messageMap.get(SENDER_ID)).longValue());
        ltMessage.setReceiverId(((Integer) messageMap.get(RECEIVER_ID)).longValue());
        ltMessage.setRelationId((Integer) messageMap.get(RELATION_ID));
        ltMessage.setSendTime(LocalDateTime.now());
        ltMessage.setMessageType(LTMessageType.MESSAGE);
        // 保存消息到数据库
        LTWechatMessageVo messageVo = ltWeChatMessageService.add(ltMessage);
        // 封装消息响应对象
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessageType((LTChatType) messageMap.get(MESSAGE_TYPE));
        messageResponse.setMessage(messageVo);
        messageResponse.setOrder(ltOrder);
        if (newStatus.getCode() > LTOrderStatus.STATUS_UNKNOWN.getCode() &&
                newStatus.getCode() < LTOrderStatus.STATUS_CANCELLED.getCode()) {
            messageResponse.setNewStatus(newStatus);
        }
        // 序列化消息为JSON字符串并发送
        String jsonMessage = objectMapper.writeValueAsString(messageResponse);
        // 发送消息给当前 chatId 下的所有 session
        sendMessageToChatId(chatId, jsonMessage);
        // 保存消息成功后，调用发送通知的方法给receiverId对应的用户
        notificationWebSocketHandler.sendNotification(messageVo.getReceiverId(), messageResponse);
    }

    private void handleChatMessageRead(WebSocketSession session, Map<String, Object> messageMap) throws IOException {
        Integer chatId = (Integer) session.getAttributes().get(CHAT_ID);
        // 从messageMap解析出未读消息ID列表
        Object value = messageMap.get(NO_READ_IDS);
        if (value instanceof List<?> tempList) {
            List<Integer> noReadIds = tempList.stream()
                    .filter(item -> item instanceof Integer)
                    .map(item -> (Integer) item)
                    .collect(Collectors.toList());
            // 调用批量更新未读消息为已读的方法
            boolean read = ltWeChatMessageService.batchRead(noReadIds);
            if (read) {
                // 序列化消息为JSON字符串并发送
                MessageResponse messageResponse = new MessageResponse();
                messageResponse.setMessageType((LTChatType) messageMap.get(MESSAGE_TYPE));
                // 设置已读消息ID列表
                messageResponse.setReadIds(noReadIds);
                String jsonMessage = objectMapper.writeValueAsString(messageResponse);
                // 发送消息给当前 chatId 下的所有 session
                sendMessageToChatId(chatId, jsonMessage);
            }
        } else {
            throw new IllegalArgumentException("Expected a list");
        }
    }

    // 聊天消息处理方法
    private void handleChatMessage(WebSocketSession session, Map<String, Object> messageMap) throws IOException {
        Integer chatId = (Integer) session.getAttributes().get(CHAT_ID);
        LTWechatMessageVo ltMessage = new LTWechatMessageVo();
        // 解析消息内容
        ltMessage.setContent((String) messageMap.get(CONTENT));
        BooleanStatus isImage = (BooleanStatus) messageMap.get(IS_IMAGE);
        if (!ObjectUtils.isEmpty(isImage) && isImage == BooleanStatus.TRUE) {
            ltMessage.setIsImage(BooleanStatus.TRUE);
        }
        ltMessage.setSenderId(((Integer) messageMap.get(SENDER_ID)).longValue());
        ltMessage.setReceiverId(((Integer) messageMap.get(RECEIVER_ID)).longValue());
        ltMessage.setRelationId((Integer) messageMap.get(RELATION_ID));
        ltMessage.setSendTime(LocalDateTime.now());
        ltMessage.setMessageType(LTMessageType.MESSAGE);
        // 保存消息到数据库
        LTWechatMessageVo messageVo = ltWeChatMessageService.add(ltMessage);

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage(messageVo);
        messageResponse.setMessageType((LTChatType) messageMap.get(MESSAGE_TYPE));
        // 将message转换为JSON字符串
        String jsonMessage = objectMapper.writeValueAsString(messageResponse);

        // 发送消息给当前 chatId 下的所有 session
        sendMessageToChatId(chatId, jsonMessage);
        // 保存消息成功后，调用发送通知的方法给receiverId对应的用户
        notificationWebSocketHandler.sendNotification(messageVo.getReceiverId(), messageResponse);
    }

    private void sendMessageToChatId(Integer chatId, String jsonMessage) throws IOException {
        List<WebSocketSession> chatSessions = sessions.get(chatId);
        if (chatSessions != null) {
            for (WebSocketSession session : chatSessions) {
                if (session.isOpen()) {
                    log.debug("Sending message：{} to session: {}", jsonMessage, session.getId());
                    session.sendMessage(new TextMessage(jsonMessage));
                } else {
                    log.warn("Session for chatId: {} and session: {} is closed", chatId, session.getId());
                }
            }
        } else {
            log.warn("No sessions found for chatId: {}", chatId);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable ex) throws Exception {
        log.error("WebSocket transport error: session={}", session, ex);
    }

}
