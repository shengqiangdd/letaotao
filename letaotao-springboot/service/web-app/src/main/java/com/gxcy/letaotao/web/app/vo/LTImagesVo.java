package com.gxcy.letaotao.web.app.vo;

import com.gxcy.letaotao.common.enums.LTImagesType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author csq
 * @Date 2024/9/13 22:53
 */
@Data
@NoArgsConstructor
public class LTImagesVo implements Serializable {
    /**
     * 图片唯一标识
     */
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
}
