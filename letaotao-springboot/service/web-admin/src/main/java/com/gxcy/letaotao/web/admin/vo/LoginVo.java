package com.gxcy.letaotao.web.admin.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class LoginVo {
    @Parameter(name = "用户名")
    @NotEmpty(message = "用户名不能为空")
    private String username;

    @Parameter(name = "密码")
    @NotEmpty(message = "密码不能为空")
    private String password;
}
