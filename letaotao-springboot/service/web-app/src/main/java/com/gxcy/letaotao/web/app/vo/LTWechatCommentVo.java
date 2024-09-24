package com.gxcy.letaotao.web.app.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.gxcy.letaotao.common.enums.BooleanStatus;
import com.gxcy.letaotao.common.enums.LTCommentType;
import com.gxcy.letaotao.common.enums.LTLikeTargetType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class LTWechatCommentVo implements Serializable {

    /**
     * 留言/评论唯一标识
     */
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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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

    private Long publisherId;

    private LTLikeTargetType likeType;

    /**
     * 用户昵称
     */
    private String nickName;


    private String targetName;


    private String avatar;


    private List<LTWechatCommentVo> child;


    private Boolean expand = false;


    private Boolean isSeller = false;


    private BooleanStatus isLiked;

}
