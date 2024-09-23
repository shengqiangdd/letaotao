package com.gxcy.letaotao.web.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxcy.letaotao.web.admin.service.BaseService;
import com.gxcy.letaotao.web.admin.vo.UserVo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements BaseService<T> {

    @Override
    public UserVo getCurrentUser() {
        // 从Spring Security上下文获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication.getPrincipal() instanceof String)) {
            // 已认证的用户主体为User类型
            return (UserVo) authentication.getPrincipal();
        } else {
            // 用户未认证，返回null
            return null;
        }
    }

    @Override
    public T findByCondition(String condition, String value) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(condition, value);
        return baseMapper.selectOne(queryWrapper);
    }
}
