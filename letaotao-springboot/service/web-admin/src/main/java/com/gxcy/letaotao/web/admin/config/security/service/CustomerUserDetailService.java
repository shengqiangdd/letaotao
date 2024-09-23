package com.gxcy.letaotao.web.admin.config.security.service;

import com.gxcy.letaotao.web.admin.mapper.UserMapper;
import com.gxcy.letaotao.web.admin.service.PermissionService;
import com.gxcy.letaotao.web.admin.vo.PermissionVo;
import com.gxcy.letaotao.web.admin.vo.UserVo;
import jakarta.annotation.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * SpringSecurity用户详情服务
 */
@Service
public class CustomerUserDetailService implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private PermissionService permissionService;

    @Override
    public UserVo loadUserByUsername(String username) throws UsernameNotFoundException {
        // 调用根据用户名查询用户信息的方法
        UserVo user = userMapper.findUserByUserName(username);
        if (user != null) {
            // 查询用户的拥有的权限列表
            List<PermissionVo> permissionList =
                    permissionService.findPermissionListByUserId(user.getId());
            // 获取权限编码
            String[] codes = permissionList.stream()
                    .filter(Objects::nonNull)
                    .map(PermissionVo::getCode)
                    .filter(Objects::nonNull)
                    .toArray(String[]::new);
            // 设置权限列表
            List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(codes);
            user.setAuthorities(authorityList);
            // 设置菜单列表
            user.setPermissionList(permissionList);
        }
        // 如果用户为空，返回null，可以进行用户注册流程
        return user;
    }

}
