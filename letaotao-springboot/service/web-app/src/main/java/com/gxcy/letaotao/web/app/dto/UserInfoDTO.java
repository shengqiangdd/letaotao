package com.gxcy.letaotao.web.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gxcy.letaotao.common.enums.GenderType;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class UserInfoDTO {

    private Long userId;
    private String avatarUrl;
    private String avatar;
    private String introduction;
    private GenderType gender;
    private String birthday;
    private String nickName;

}
