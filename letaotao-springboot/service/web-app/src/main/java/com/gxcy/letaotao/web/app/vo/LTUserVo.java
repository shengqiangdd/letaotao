package com.gxcy.letaotao.web.app.vo;

import com.gxcy.letaotao.common.entity.BaseUser;
import com.gxcy.letaotao.common.enums.BooleanStatus;
import com.gxcy.letaotao.common.enums.GenderType;
import com.gxcy.letaotao.common.enums.LTUserFollowStatus;
import com.gxcy.letaotao.web.app.config.validator.UniqueUser;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author csq
 * @Date 2024/9/13 20:55
 */
@Data
public class LTUserVo implements Serializable, BaseUser {
    @Parameter(name = "用户编号")
    private Long id;

    @Parameter(name = "登录名称(用户名)")
    @UniqueUser(message = "用户名已存在")
    private String username;

    @Parameter(name = "真实姓名")
    private String realName;

    @Parameter(name = "昵称")
    private String nickName;

    private String password;

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

    @Parameter(name = "微信openId")
    private String openId;

    @Parameter(name = "创建时间")
    private LocalDateTime createTime;

    @Parameter(name = "修改时间")
    private LocalDateTime updateTime;

    @Parameter(name = "是否删除(0-未删除，1-已删除)")
    private BooleanStatus isDelete;

    @Parameter(name = "是否已关注")
    private LTUserFollowStatus isFollowed;

    @Parameter(name = "粉丝数量")
    private Integer followerCount = 0;

    @Parameter(name = "关注数量")
    private Integer followingCount = 0;

    private Boolean isNewUser = false;
}
