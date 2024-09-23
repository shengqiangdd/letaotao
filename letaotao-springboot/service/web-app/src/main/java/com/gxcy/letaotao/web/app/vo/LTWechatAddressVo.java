package com.gxcy.letaotao.web.app.vo;

import com.gxcy.letaotao.common.enums.BooleanStatus;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * @Author csq
 * @Date 2024/9/10 11:29
 */
@Data
public class LTWechatAddressVo implements Serializable {
    /**
     * 地址唯一标识
     */
    private Integer id;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 联系人姓名
     */
    @NotEmpty(message = "联系人姓名不能为空")
    private String contactPerson;

    /**
     * 联系电话
     */
    @NotEmpty(message = "联系电话不能为空")
    @Length(min = 11, max = 11, message = "联系电话长度为11位")
    private String phone;

    /**
     * 地区
     */
    @NotEmpty(message = "地区不能为空")
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
