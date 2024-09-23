package com.gxcy.letaotao.web.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.gxcy.letaotao.common.enums.BooleanStatus;
import com.gxcy.letaotao.common.enums.GenderType;
import com.gxcy.letaotao.web.admin.config.validator.UniqueUser;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("sys_user")
@Tag(name = "User对象", description = "")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Parameter(name = "用户编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Parameter(name = "登录名称(用户名)")
    @UniqueUser(message = "用户名已存在")
    private String username;

    @Parameter(name = "登录密码")
    private String password;

    @Parameter(name = "帐户是否过期(1-未过期，0-已过期)")
    private boolean isAccountNonExpired;

    @Parameter(name = "帐户是否被锁定(1-未锁定，0-锁定)")
    private boolean isAccountNonLocked;

    @Parameter(name = "密码是否过期(1-未过期，0-已过期)")
    private boolean isCredentialsNonExpired;

    @Parameter(name = "帐户是否可用(1-可用，0-禁用)")
    private boolean isEnabled;

    @Parameter(name = "真实姓名")
    private String realName;

    @Parameter(name = "昵称")
    private String nickName;

    @Parameter(name = "性别(0-男，1-女)")
    private GenderType gender;

    @Parameter(name = "电话")
    private String phone;

    @Parameter(name = "邮箱")
    private String email;

    @Parameter(name = "用户头像")
    private String avatar;

    @Parameter(name = "个人介绍")
    private String introduction = "";

    @Parameter(name = "生日")
    private String birthday = "";

    @Parameter(name = "是否是管理员(1-管理员)")
    private BooleanStatus isAdmin;

    @Parameter(name = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Parameter(name = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Parameter(name = "是否删除(0-未删除，1-已删除)")
    private BooleanStatus isDelete;

}
