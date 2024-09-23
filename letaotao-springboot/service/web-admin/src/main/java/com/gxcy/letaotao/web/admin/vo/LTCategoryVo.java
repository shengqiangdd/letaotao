package com.gxcy.letaotao.web.admin.vo;

import com.gxcy.letaotao.common.enums.BooleanStatus;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author csq
 * @Date 2024/9/10 9:55
 */
@Data
public class LTCategoryVo implements Serializable {
    /**
     * 分类唯一标识
     */
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
     * 父分类名称
     */
    private String parentName;

    /**
     * 分类图片URL
     */
    private String imageUrl;

    private BooleanStatus isDelete;
}
