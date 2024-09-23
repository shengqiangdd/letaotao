package com.gxcy.letaotao.web.app.vo;

import com.gxcy.letaotao.common.enums.BooleanStatus;
import com.gxcy.letaotao.common.enums.LTProductStatus;
import lombok.Data;

@Data
public class LTWechatProductQueryVo {

    private Long pageNo = 1L; // 当前页码
    private Long pageSize = 10L; // 每页显示数量

    private String[] betweenTime; // 开始-结束时间

    private Integer tabValue;

    private String label;

    private Long userId;
    private String productValue;
    private String sortValue;
    private String conditionValue;
    private String nickName;

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

    private LTProductStatus status;

    /**
     * 是否推荐
     */
    private BooleanStatus isRecommended;

    /**
     * 发布者ID
     */
    private Long publisherId;

}
