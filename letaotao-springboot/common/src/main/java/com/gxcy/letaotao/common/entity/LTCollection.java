package com.gxcy.letaotao.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gxcy.letaotao.common.enums.BooleanStatus;
import com.gxcy.letaotao.common.enums.LTCollectionTargetType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 收藏表
 */
@Data
@TableName("lt_collection")
@NoArgsConstructor
public class LTCollection implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 收藏唯一标识
     */
    @TableId(value = "id", type = IdType.AUTO)
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
    @TableField(fill = FieldFill.INSERT)
    private Timestamp createTime;

    /**
     * 收藏状态
     */
    private BooleanStatus isActive;

}
