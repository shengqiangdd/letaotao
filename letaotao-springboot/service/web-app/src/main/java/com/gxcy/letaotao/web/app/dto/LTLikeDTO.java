package com.gxcy.letaotao.web.app.dto;

import com.gxcy.letaotao.common.enums.LTLikeTargetType;
import lombok.Data;

/**
 * @Author csq
 * @Date 2024/9/14 11:09
 */
@Data
public class LTLikeDTO {
    /**
     * 点赞用户ID
     */
    private Long userId;

    /**
     * 点赞目标ID
     */
    private Integer targetId;

    /**
     * 点赞目标类型（1: 帖子, 2: 评论，3：留言）
     */
    private LTLikeTargetType targetType;
}
