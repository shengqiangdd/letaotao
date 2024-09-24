package com.gxcy.letaotao.web.app.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gxcy.letaotao.common.enums.LTUserFollowStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 帖子表
 */
@Data
@AllArgsConstructor
@TableName("lt_user_follow")
@NoArgsConstructor
public class LTUserFollow implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 关注唯一标识符
     */
    private Long id;
    /**
     * 关注者
     */
    private Long followerId;
    /**
     * 被关注者
     */
    private Long followedId;
    /**
     * 关注状态（0:未关注, 1:已关注）
     */
    private LTUserFollowStatus status;
    /**
     * 关注时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
