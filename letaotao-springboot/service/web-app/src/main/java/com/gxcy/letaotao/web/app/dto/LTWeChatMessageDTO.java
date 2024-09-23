package com.gxcy.letaotao.web.app.dto;

import com.gxcy.letaotao.common.enums.LTMessageType;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * 微信消息传输对象
 */
@Data
public class LTWeChatMessageDTO {

    @NotEmpty(message = "内容不能为空")
    private String content;
    private LTMessageType messageType;
    private Long senderId;
    private Long receiverId;
    private Integer relationId;

}
