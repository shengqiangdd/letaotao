package com.gxcy.letaotao.web.app.vo;

import com.gxcy.letaotao.common.entity.LTOrder;
import com.gxcy.letaotao.common.enums.LTChatType;
import com.gxcy.letaotao.common.enums.LTOrderMessageStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class MessageResponse implements Serializable {

    private LTWechatMessageVo message;
    private LTChatType messageType;
    private LTOrderMessageStatus newStatus;
    private LTOrder order;
    private List<Integer> readIds;
}
