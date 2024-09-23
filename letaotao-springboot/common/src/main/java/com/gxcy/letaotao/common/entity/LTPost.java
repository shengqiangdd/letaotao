package com.gxcy.letaotao.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gxcy.letaotao.common.enums.LTPostStatus;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 帖子表
 */
@Data
@TableName("lt_post")
public class LTPost implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 帖子唯一标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 发帖用户ID
     */
    private Long userId;

    /**
     * 帖子标题
     */
    private String title;

    /**
     * 帖子内容
     */
    private String content;

    /**
     * 帖子图片URL
     */
    private String imageUrl;

    /**
     * 帖子状态（0：草稿，1：发布，3：待审核）
     */
    private LTPostStatus status;

    /**
     * 帖子发布时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime postTime;

    /**
     * 帖子更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 点赞数量
     */
    private Integer likeCount = 0;

    /**
     * 评论数量
     */
    private Integer commentCount = 0;

    /**
     * 收藏数量
     */
    private Integer collectCount = 0;

}
