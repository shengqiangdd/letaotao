package com.gxcy.letaotao.web.app.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gxcy.letaotao.common.enums.BooleanStatus;
import com.gxcy.letaotao.common.enums.LTMessageType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author csq
 * @Date 2024/9/9 23:06
 */
@Data
@NoArgsConstructor
public class LTWechatMessageVo implements Serializable {

    /**
     * 消息唯一标识
     */
    private Integer id;

    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息类型（1：通知，2：消息）
     */
    private LTMessageType messageType;

    /**
     * 发送者ID
     */
    private Long senderId;

    /**
     * 接收者ID
     */
    private Long receiverId;

    /**
     * 消息发送时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sendTime;

    /**
     * 聊天关系编号
     */
    private Integer relationId;

    private BooleanStatus isImage;

    private BooleanStatus isRead = BooleanStatus.FALSE;

    private BooleanStatus isOrder;

    private Integer orderId;

    private BooleanStatus isDelete;

    private String senderName;


    private String receiverName;


    private String nickName;


    private String avatar;
}
