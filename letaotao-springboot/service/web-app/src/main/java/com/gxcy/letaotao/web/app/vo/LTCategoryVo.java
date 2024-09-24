package com.gxcy.letaotao.web.app.vo;

import com.gxcy.letaotao.common.enums.BooleanStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author csq
 * @Date 2024/9/13 22:56
 */
@Data
@NoArgsConstructor
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
     * 分类图片URL
     */
    private String imageUrl;

    private BooleanStatus isDelete;
}
