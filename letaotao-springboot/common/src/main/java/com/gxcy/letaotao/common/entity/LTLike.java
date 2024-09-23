package com.gxcy.letaotao.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gxcy.letaotao.common.enums.BooleanStatus;
import com.gxcy.letaotao.common.enums.LTLikeTargetType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@TableName("lt_like")
public class LTLike implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 点赞唯一标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

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

    /**
     * 点赞时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Timestamp createTime;

    /**
     * 点赞状态
     */
    private BooleanStatus isActive;

}
