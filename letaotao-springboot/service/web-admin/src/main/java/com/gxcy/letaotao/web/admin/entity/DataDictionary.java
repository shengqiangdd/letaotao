package com.gxcy.letaotao.web.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @TableName data_dictionary
 */
@TableName(value = "data_dictionary")
@Schema(description = "数据字典实体")
@Data
@NoArgsConstructor
public class DataDictionary implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 逻辑删除(1:已删除，0:未删除)
     */
    private Integer isDeleted;

    /**
     * 上级ID
     */
    private Long parentId;

    /**
     * 名称
     */
    private String name;

    /**
     * 字符串类型的值
     */
    private String strValue;

    /**
     * 数值类型的值
     */
    private Integer numValue;

    /**
     * 编码
     */
    private String dictCode;

}