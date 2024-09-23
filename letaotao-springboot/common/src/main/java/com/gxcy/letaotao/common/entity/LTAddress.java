package com.gxcy.letaotao.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gxcy.letaotao.common.enums.BooleanStatus;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 收货地址表
 */
@Data
@TableName("lt_address")
public class LTAddress implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 地址唯一标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 联系人姓名
     */
    private String contactPerson;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 地区
     */
    private String region;

    /**
     * 详细地址
     */
    private String detailAddress;

    /**
     * 是否默认
     */
    private BooleanStatus isDefault;


}
