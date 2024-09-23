package com.gxcy.letaotao.web.app.vo;

import com.gxcy.letaotao.common.enums.LTOrderStatus;
import lombok.Data;

@Data
public class LTWechatOrderQueryVo {

    private Long pageNo = 1L; // 当前页码
    private Long pageSize = 10L; // 每页显示数量

    private String startDate;
    private String endDate;

    private String searchValue;

    private String productName;
    /**
     * 订单号
     */
    private String orderNum;
    private LTOrderStatus status;

    /**
     * 买家ID
     */
    private Long buyerId;

    /**
     * 卖家ID
     */
    private Long sellerId;

}
