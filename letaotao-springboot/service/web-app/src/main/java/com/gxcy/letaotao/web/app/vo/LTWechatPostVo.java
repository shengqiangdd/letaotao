package com.gxcy.letaotao.web.app.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gxcy.letaotao.common.enums.BooleanStatus;
import com.gxcy.letaotao.common.enums.LTPostStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author csq
 * @Date 2024/9/9 23:09
 */
@Data
@NoArgsConstructor
public class LTWechatPostVo implements Serializable {

    /**
     * 帖子唯一标识
     */
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

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 粉丝数量
     */
    private Integer following = 0;


    private List<LTImagesVo> images;


    private BooleanStatus isLiked;


    private BooleanStatus isFavorite;


    private BooleanStatus isFollowed;


    private Integer followerCount = 0;
}
