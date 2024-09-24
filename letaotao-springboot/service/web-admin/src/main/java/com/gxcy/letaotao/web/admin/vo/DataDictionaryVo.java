package com.gxcy.letaotao.web.admin.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author csq
 * @Date 2024/9/15 13:55
 */
@Data
@NoArgsConstructor
public class DataDictionaryVo implements Serializable {
    /**
     * ID
     */
    private Long id;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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

    private String parentName;

    private List<DataDictionaryVo> children;

}
