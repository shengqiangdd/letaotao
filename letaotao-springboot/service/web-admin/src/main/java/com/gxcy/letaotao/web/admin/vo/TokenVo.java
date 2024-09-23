package com.gxcy.letaotao.web.admin.vo;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenVo {
    @Parameter(name = "过期时间")
    private Long expireTime; // 过期时间

    @Parameter(name = "token")
    private String token; // token
}
