package com.gxcy.letaotao.web.app.vo;

import com.gxcy.letaotao.common.enums.GenderType;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfoVo implements Serializable {

    private Long id;
    private String username;
    private String realName;
    private String nickName;
    private GenderType gender;
    private String phone;
    private String avatar;
    private String introduction;
    private String birthday;
    private String openId;
    private Integer followerCount;
    private Integer followingCount;
    private Boolean isNewUser;
}
