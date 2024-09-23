package com.gxcy.letaotao.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @Author csq
 * @Date 2024/9/10 10:10
 */
@Getter
public enum LTOrderStatus implements BaseEnum {

    STATUS_UNKNOWN(0, "未知"),
    STATUS_PENDING_PAYMENT(1, "待付款"),
    STATUS_PENDING_DELIVERY(2, "待发货"),
    STATUS_Pending_Received(3, "待收货"),
    STATUS_COMPLETED(4, "已完成"),
    STATUS_CANCELLED(5, "已取消"),
    STATUS_PENDING_EVALUATION(8, "待评价");

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String desc;

    LTOrderStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
