package com.gxcy.letaotao.web.app.vo;

import com.gxcy.letaotao.common.enums.LTCommentType;
import com.gxcy.letaotao.common.enums.LTLikeTargetType;
import lombok.Data;

/**
 * @Author csq
 * @Date 2024/9/13 22:34
 */
@Data
public class LTWechatCommentQueryVo {

    private Long pageNo = 1L;//当前页码
    private Long pageSize = 10L;//每页显示数量

    /**
     * 留言/评论类型（1：留言，2：评论）
     */
    private LTCommentType type;

    /**
     * 相关实体ID
     */
    private Integer referenceId;

    private Long publisherId;

    private LTLikeTargetType likeType;

    /**
     * 留言/评论用户ID
     */
    private Long userId;

}
