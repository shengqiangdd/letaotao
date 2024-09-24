package com.gxcy.letaotao.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gxcy.letaotao.common.enums.BooleanStatus;
import com.gxcy.letaotao.common.enums.LTMessageType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通知/消息表
 */
@Data
@TableName("lt_message")
@NoArgsConstructor
public class LTMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 消息唯一标识
     */
    @TableId(value = "id", type = IdType.AUTO)
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
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime sendTime;

    /**
     * 聊天关系编号
     */
    private Integer relationId;

    private BooleanStatus isImage;

    private BooleanStatus isRead;

    private BooleanStatus isOrder;

    private Integer orderId;

    private BooleanStatus isDelete;

}
