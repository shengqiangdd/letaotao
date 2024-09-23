package com.gxcy.letaotao.web.app.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.gxcy.letaotao.common.enums.LTImagesType;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author csq
 * @Date 2024/9/13 22:53
 */
@Data
public class LTImagesVo implements Serializable {
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
}
