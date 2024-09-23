package com.gxcy.letaotao.web.admin.vo;

import com.gxcy.letaotao.common.enums.LTMessageType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LTMessageQueryVo {

    private Long pageNo = 1L; // 当前页码
    private Long pageSize = 10L; // 每页显示数量

    private LTMessageType type;
    private String nickName;

    /**
     * 消息内容
     */
    private String content;

    private LocalDateTime sendTime;
}
