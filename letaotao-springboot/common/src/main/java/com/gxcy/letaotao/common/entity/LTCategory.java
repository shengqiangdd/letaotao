package com.gxcy.letaotao.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gxcy.letaotao.common.enums.BooleanStatus;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 商品分类表
 */
@Data
@TableName("lt_category")
public class LTCategory implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分类唯一标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 父分类ID
     */
    private Integer parentId;

    /**
     * 分类图片URL
     */
    private String imageUrl;

    private BooleanStatus isDelete;

}
