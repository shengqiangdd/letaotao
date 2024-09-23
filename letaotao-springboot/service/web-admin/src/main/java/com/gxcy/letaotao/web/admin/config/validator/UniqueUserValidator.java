package com.gxcy.letaotao.web.admin.config.validator;

import com.gxcy.letaotao.web.admin.entity.User;
import com.gxcy.letaotao.web.admin.service.UserService;
import jakarta.annotation.Resource;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class UniqueUserValidator implements ConstraintValidator<UniqueUser, String> {

    @Resource
    private UserService userService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 查询数据库中是否存在该用户
        Long count = userService.lambdaQuery()
                .eq(User::getUsername, value)
                .count();
        return count == 0;
    }
}