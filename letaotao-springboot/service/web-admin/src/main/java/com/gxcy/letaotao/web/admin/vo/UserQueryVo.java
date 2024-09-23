package com.gxcy.letaotao.web.admin.vo;

import com.gxcy.letaotao.common.enums.GenderType;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

@Data
public class UserQueryVo {
    private Long pageNo = 1L; // 当前页码
    private Long pageSize = 10L; // 每页显示数量

    @Parameter(name = "真实姓名")
    private String realName;

    @Parameter(name = "性别(0-男，1-女)")
    private GenderType gender;

    @Parameter(name = "电话")
    private String phone;

    @Parameter(name = "登录名称(用户名)")
    private String username;
}
