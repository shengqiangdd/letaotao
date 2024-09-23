package com.gxcy.letaotao.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @Author csq
 * @Date 2024/9/10 10:27
 */
@Getter
public enum LTOrderMessageStatus implements BaseEnum {

    AlreadyBuy(1, "我已拍下，待付款", "我已下单，等待我的付款"),
    AlreadyPay(2, "我已付款，等待你发货", "请带上物品，按照我提供的地址送货"),
    AlreadyShip(3, "我已发货，等待你收货", "我正在送货路上，请等待收货"),
    AlreadyFinish(4, "交易完成，等待评价", "双方交易完成，请及时完成评价"),
    AlreadyCancel(5, "交易取消", "我已取消订单"),
    AlreadyEvaluate(6, "完成评价", "我已完成评价");

    @JsonValue
    private final Integer code;
    private final String title;
    private final String content;

    LTOrderMessageStatus(Integer code, String title, String content) {
        this.code = code;
        this.title = title;
        this.content = content;
    }

    @Override
    public String getDesc() {
        return "";
    }
}
