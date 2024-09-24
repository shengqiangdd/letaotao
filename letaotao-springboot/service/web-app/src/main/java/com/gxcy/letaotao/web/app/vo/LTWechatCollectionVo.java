package com.gxcy.letaotao.web.app.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gxcy.letaotao.common.enums.BooleanStatus;
import com.gxcy.letaotao.common.enums.LTCollectionTargetType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Author csq
 * @Date 2024/9/9 23:00
 */
@Data
@NoArgsConstructor
public class LTWechatCollectionVo implements Serializable {

    /**
     * 收藏唯一标识
     */
    private Integer id;

    /**
     * 收藏用户ID
     */
    private Long userId;

    /**
     * 收藏目标ID
     */
    private Integer targetId;

    /**
     * 收藏目标类型（1: 商品, 2: 帖子）
     */
    private LTCollectionTargetType targetType;

    /**
     * 收藏时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;

    /**
     * 收藏状态
     */
    private BooleanStatus isActive;

    private LTWechatProductVo product;


    private LTWechatPostVo post;


    private Boolean check = false;
}
