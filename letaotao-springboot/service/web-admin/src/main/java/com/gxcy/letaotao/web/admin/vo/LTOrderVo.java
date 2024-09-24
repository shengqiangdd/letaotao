package com.gxcy.letaotao.web.admin.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gxcy.letaotao.common.enums.BooleanStatus;
import com.gxcy.letaotao.common.enums.LTOrderStatus;
import com.gxcy.letaotao.web.admin.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @Author csq
 * @Date 2024/9/9 23:07
 */
@Data
@NoArgsConstructor
public class LTOrderVo implements Serializable {

    /**
     * 订单唯一标识
     */
    private Integer id;

    /**
     * 订单号
     */
    private String orderNum;

    /**
     * 买家ID
     */
    private Long buyerId;

    /**
     * 卖家ID
     */
    private Long sellerId;

    /**
     * 商品ID
     */
    private Integer productId;

    /**
     * 订单金额
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private BigDecimal price;

    /**
     * 收货地址ID
     */
    private Integer addressId;

    /**
     * 订单状态（待付款：1，待发货：2，待收货：3，已完成：4，已取消：5）
     */
    private LTOrderStatus status;

    /**
     * 下单时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;

    /**
     * 支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp payTime;

    /**
     * 发货时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp shipTime;

    /**
     * 完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp finishTime;

    /**
     * 微信交易号
     */
    private String transactionNum;

    private BooleanStatus isDelete;

    /**
     * 买家昵称
     */
    private String buyerName;

    /**
     * 卖家昵称
     */
    private String sellerName;

    private String addressName;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 收货地址
     */
    private String address;

    private String userName;

    private String avatar;

    private String productImage;

    private BigDecimal productPrice;

    private Boolean isEvaluate;

    private User seller;

    private User buyer;

    private LTProductVo product;

    private Integer relationId;

}
