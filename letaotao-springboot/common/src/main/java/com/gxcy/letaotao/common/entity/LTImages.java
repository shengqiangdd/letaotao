package com.gxcy.letaotao.common.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gxcy.letaotao.common.enums.LTImagesType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 图片表
 */
@Data
@TableName("lt_images")
@NoArgsConstructor
public class LTImages implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 图片唯一标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 图片地址
     */
    private String url;

    /**
     * 关联编号
     */
    private Integer relatedId;

    /**
     * 关联类型（product、post、chat、user）
     */
    private LTImagesType type;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Timestamp createTime;
}
