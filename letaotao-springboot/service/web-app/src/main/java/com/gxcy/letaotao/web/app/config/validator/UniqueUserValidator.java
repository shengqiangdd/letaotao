package com.gxcy.letaotao.web.app.config.validator;

import com.gxcy.letaotao.web.app.entity.LTUser;
import com.gxcy.letaotao.web.app.service.WeChatUserService;
import jakarta.annotation.Resource;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class UniqueUserValidator implements ConstraintValidator<UniqueUser, String> {

    @Resource
    private WeChatUserService userService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 查询数据库中是否存在该用户
        Long count = userService.lambdaQuery()
                .eq(LTUser::getUsername, value)
                .count();
        return count == 0;
    }
}