package com.gxcy.letaotao.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gxcy.letaotao.common.enums.BooleanStatus;
import com.gxcy.letaotao.common.enums.LTProductStatus;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品表
 */
@Data
@TableName("lt_product")
public class LTProduct implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 商品唯一标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 发布者ID
     */
    private Long publisherId;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品分类
     */
    private Integer categoryId;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 商品成色
     */
    @TableField(value = "`condition`")
    private String condition;

    /**
     * 商品价格
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private BigDecimal price;

    /**
     * 商品发布时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;

    /**
     * 商品更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 商品状态（0：草稿，1：上架，2：下架，3：待审核，4：已售出）
     */
    private LTProductStatus status;

    /**
     * 是否推荐
     */
    private BooleanStatus isRecommended;

    private BooleanStatus isDelete;

    private BooleanStatus isLock;

    private String lockBy;

}
