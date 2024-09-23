package com.gxcy.letaotao.web.app.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gxcy.letaotao.common.enums.LTOrderStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class LTWeChatOrderVo implements Serializable {
    private Integer id; // 订单id

    @NotEmpty(message = "订单编号不能为空")
    private String orderNum; // 订单编号

    @NotNull(message = "买家id不能为空")
    private Long buyerId; // 买家id

    @NotNull(message = "卖家id不能为空")
    private Long sellerId; // 卖家id

    @NotNull(message = "商品id不能为空")
    private Integer productId; // 商品id

    @NotNull(message = "地址id不能为空")
    private Integer addressId; // 地址id

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    @NotNull(message = "价格不能为空")
    @Positive(message = "价格必须大于0")
    private BigDecimal price; // 价格

    private String timeout; // 超时时间

    private LTOrderStatus status;

    private Boolean isEvaluate;

    private Long currentUserId;

    private LTUserVo seller;
    private LTUserVo buyer;
    private LTWechatProductVo product;
    private Integer relationId;
    private String addressName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp payTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp shipTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp finishTime;

    private String productName;
    private String productImage;

    private String transactionNum;

}
