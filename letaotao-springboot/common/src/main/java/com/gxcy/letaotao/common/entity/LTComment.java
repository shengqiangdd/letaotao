package com.gxcy.letaotao.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gxcy.letaotao.common.enums.BooleanStatus;
import com.gxcy.letaotao.common.enums.LTCommentType;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 留言/评论表
 */
@Data
@TableName("lt_comment")
public class LTComment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 留言/评论唯一标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 留言/评论用户ID
     */
    private Long userId;

    /**
     * 相关实体ID
     */
    private Integer referenceId;

    /**
     * 父评论ID
     */
    private Integer parentId;

    /**
     * 留言/评论内容
     */
    private String content;

    /**
     * 留言/评论时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime commentTime;

    /**
     * 留言/评论类型（1：留言，2：评论）
     */
    private LTCommentType type;

    /**
     * 点赞数量
     */
    private Integer likeCount;

    private BooleanStatus isDelete;

}
