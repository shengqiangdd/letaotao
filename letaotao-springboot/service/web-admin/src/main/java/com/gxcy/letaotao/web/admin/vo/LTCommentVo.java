package com.gxcy.letaotao.web.admin.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gxcy.letaotao.common.enums.BooleanStatus;
import com.gxcy.letaotao.common.enums.LTCommentType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author csq
 * @Date 2024/9/9 23:05
 */
@Data
public class LTCommentVo implements Serializable {

    /**
     * 留言/评论唯一标识
     */
    private Integer id;

    /**
     * 留言/评论用户ID
     */
    @NotNull(message = "留言/评论用户ID不能为空")
    private Long userId;

    /**
     * 相关实体ID
     */
    @NotNull(message = "留言/评论实体ID不能为空")
    private Integer referenceId;

    /**
     * 父评论ID
     */
    private Integer parentId;

    /**
     * 留言/评论内容
     */
    @NotEmpty(message = "留言/评论内容不能为空")
    private String content;

    /**
     * 留言/评论时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime commentTime;

    /**
     * 留言/评论类型（1：留言，2：评论）
     */
    @NotNull(message = "留言/评论类型不能为空")
    private LTCommentType type;

    /**
     * 点赞数量
     */
    private Integer likeCount;

    private BooleanStatus isDelete;

    /**
     * 用户昵称
     */
    private String nickName;


    private String targetName;


    private String avatar;


    private List<LTCommentVo> child;

}
