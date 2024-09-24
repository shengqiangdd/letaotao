package com.gxcy.letaotao.web.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gxcy.letaotao.common.config.redis.CacheKeyConstants;
import com.gxcy.letaotao.common.config.redis.RedisService;
import com.gxcy.letaotao.common.enums.BooleanStatus;
import com.gxcy.letaotao.common.enums.PermissionType;
import com.gxcy.letaotao.common.exception.BizException;
import com.gxcy.letaotao.common.exception.BizExceptionEnum;
import com.gxcy.letaotao.common.utils.JwtUtils;
import com.gxcy.letaotao.common.utils.LTConstants;
import com.gxcy.letaotao.common.utils.SystemConstants;
import com.gxcy.letaotao.web.admin.config.security.handler.LoginFailureHandler;
import com.gxcy.letaotao.web.admin.config.security.handler.LoginSuccessHandler;
import com.gxcy.letaotao.web.admin.config.security.service.CustomerUserDetailService;
import com.gxcy.letaotao.web.admin.entity.User;
import com.gxcy.letaotao.web.admin.entity.UserInfo;
import com.gxcy.letaotao.web.admin.exception.CustomerAuthenticationException;
import com.gxcy.letaotao.web.admin.mapper.UserMapper;
import com.gxcy.letaotao.web.admin.service.FileService;
import com.gxcy.letaotao.web.admin.service.UserService;
import com.gxcy.letaotao.web.admin.vo.*;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private FileService fileService;
    @Resource
    private RedisService redisService;
    @Resource
    private CustomerUserDetailService userDetailService;
    @Resource
    private LoginSuccessHandler loginSuccessHandler;
    @Resource
    private LoginFailureHandler loginFailureHandler;
    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private CacheManager cacheManager;

    /**
     * 根据用户名查询用户信息
     *
     * @param username
     * @return
     */
    @Override
    @Cacheable(value = CacheKeyConstants.ADMIN_USER, key = "#result.id", unless = "#result == null")
    public UserVo findUserByUserName(String username) {
        // 返回查询记录
        return baseMapper.findUserByUserName(username);
    }

    /**
     * 分页查询用户信息
     *
     * @param page
     * @param userQueryVo
     * @return
     */
    @Override
    @Cacheable(value = CacheKeyConstants.ADMIN_USER, keyGenerator = "customKeyGenerator", unless = "#result == null")
    public IPage<UserVo> findUserListByPage(IPage<UserVo> page, UserQueryVo userQueryVo) {
        // 创建条件构造器对象
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        // 用户名
        queryWrapper.like(!ObjectUtils.isEmpty(userQueryVo.getUsername()),
                User::getUsername, userQueryVo.getUsername());
        // 真实姓名
        queryWrapper.like(!ObjectUtils.isEmpty(userQueryVo.getRealName()),
                User::getRealName, userQueryVo.getRealName());
        // 电话
        queryWrapper.like(!ObjectUtils.isEmpty(userQueryVo.getPhone()),
                User::getPhone, userQueryVo.getPhone());
        //查询并返回数据
        return baseMapper.findListByPage(page, queryWrapper);
    }

    /**
     * 删除用户信息
     *
     * @param id
     * @return
     */
    @Override
    @CacheEvict(value = CacheKeyConstants.ADMIN_USER, key = "#id")
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Long id) {
        // 查询
        User user = baseMapper.selectById(id);
        // 删除用户角色关系
        baseMapper.deleteUserRole(id);
        // 删除用户
        if (baseMapper.deleteById(id) > 0) {
            // 判断是否有头像，有则删除
            if (user != null && !ObjectUtils.isEmpty(user.getAvatar())
                    && !user.getAvatar().equals(SystemConstants.DEFAULT_AVATAR)) {
                // 删除文件
                fileService.delete(user.getAvatar());
            }
            return true;
        }
        return false;
    }

    /**
     * 分配角色
     *
     * @param userId
     * @param roleIds
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUserRole(Long userId, List<Long> roleIds) {
        // 删除该用户对应的角色信息
        baseMapper.deleteUserRole(userId);
        // 保存用户角色信息
        if (baseMapper.saveUserRole(userId, roleIds) > 0) {
            Objects.requireNonNull(cacheManager.getCache(CacheKeyConstants.ADMIN_USER)).evict(userId);
            return true;
        }
        return false;
    }

    @Override
    @Cacheable(value = CacheKeyConstants.ADMIN_USER, key = "#id", unless = "#result == null")
    public UserVo findUserById(Long id) {
        return this.convert(this.getById(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(UserVo userVo) {
        // 密码加密
        userVo.setPassword(bCryptPasswordEncoder.encode(userVo.getPassword()));
        User user = this.convert(userVo);
        if (this.save(user)) {
            this.cacheUserById(user.getId());
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheKeyConstants.ADMIN_USER, key = "#userVo.id")
    public boolean update(UserVo userVo) {
        // 查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(userVo.getUsername()), User::getUsername, userVo.getUsername());
        UserVo item = this.convert(this.getOne(queryWrapper));
        // 判断对象是否为空，且查询到的用户ID不等于当前编辑的用户ID，表示该名称被占用
        if (!Objects.equals(item.getId(), userVo.getId())) {
            throw new BizException("登录名称已被占用！");
        }
        return this.updateById(this.convert(userVo));
    }

    @CachePut(value = CacheKeyConstants.ADMIN_USER, key = "#result.id", condition = "#result != null")
    public UserVo cacheUserById(Long id) {
        return this.convert(this.getById(id));
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        // 从Header中获取前端提交的token
        String token = request.getHeader(LTConstants.TOKEN);
        // 如果header中没有token，则从参数中获取
        if (ObjectUtils.isEmpty(token)) {
            token = request.getParameter(LTConstants.TOKEN);
        }
        // 从 Spring Security 上下文获取认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 检查是否已认证
        if (authentication != null && authentication.isAuthenticated()) {
            // 清除认证信息
            SecurityContextHolder.clearContext();

            // 清空redis里面的token
            String key = CacheKeyConstants.ADMIN_TOKEN_PREFIX + token;
            redisService.del(key);

            // 执行登出操作
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
    }

    @Override
    public void login(LoginVo user, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = user.getUsername(); // 用户名
        String password = user.getPassword(); // 密码
        // 获取用户信息
        UserVo userDetails = userDetailService.loadUserByUsername(username);
        try {
            if (userDetails == null) {
                throw new CustomerAuthenticationException(BizExceptionEnum.USER_NOT_EXISTS); // 用户不存在
            }
            boolean matches = bCryptPasswordEncoder.matches(password, userDetails.getPassword());
            if (!matches) {
                throw new CustomerAuthenticationException(BizExceptionEnum.USER_PASSWORD_ERROR); // 密码不正确
            }
            // 创建身份验证对象
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails,
                            null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // 设置到Spring Security上下文
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            loginSuccessHandler.onAuthenticationSuccess(request, response, SecurityContextHolder.getContext().getAuthentication()); // 登录成功
        } catch (AuthenticationException e) {
            loginFailureHandler.onAuthenticationFailure(request, response, e); // 登录失败
        }
    }

    @Override
    public TokenVo refreshToken(HttpServletRequest request) {
        // 从Header中获取前端提交的token
        String token = request.getHeader(LTConstants.TOKEN);
        // 如果header中没有token，则从参数中获取
        if (ObjectUtils.isEmpty(token)) {
            token = request.getParameter(LTConstants.TOKEN);
        }
        UserVo user = this.getCurrentUser();
        // 重新生成token
        String reToken = "";
        // 验证原来的token是否合法
        if (jwtUtils.validateToken(token, user)) {
            //生成新的token
            reToken = jwtUtils.refreshToken(token);
        }
        // 获取本次token的到期时间，交给前端做判断
        long expireTime = jwtUtils.getClaimsFromToken(reToken).getExpiration().getTime();
        // 清除原来的token信息
        String oldTokenKey = CacheKeyConstants.ADMIN_TOKEN_PREFIX + token;
        redisService.del(oldTokenKey);
        //存储新的token
        String newTokenKey = CacheKeyConstants.ADMIN_TOKEN_PREFIX + reToken;
        redisService.set(newTokenKey, reToken, jwtUtils.getExpiration() / 1000);
        //创建TokenVo对象
        return new TokenVo(expireTime, reToken);
    }

    @Override
    public UserInfo getUserInfo() {
        // 获取用户信息
        UserVo user = this.getCurrentUser();
        // 判断用户是否存在
        if (user == null) {
            throw new BizException("用户信息查询失败");
        }
        // 用户权限集合
        List<PermissionVo> permissionList = user.getPermissionList();
        // 获取角色权限编码字段
        Object[] roles = permissionList.stream()
                .filter(Objects::nonNull)
                .map(PermissionVo::getCode).toArray();
        // 创建用户信息对象
        return new UserInfo(user.getId(), user.getNickName(),
                user.getAvatar(), null, roles);
    }

    @Override
    public List<RouterVo> getMenuList() {
        // 获取用户信息
        UserVo user = this.getCurrentUser();
        // 获取相应的权限
        List<PermissionVo> permissionList = user.getPermissionList();
        // 筛选目录和菜单
        List<PermissionVo> collect = permissionList.stream()
                .filter(item -> item != null && item.getIsDelete() != BooleanStatus.TRUE
                        && item.getType() != PermissionType.BUTTON)
                .collect(Collectors.toList());
        // 生成路由数据
        return MenuTree.makeRouter(collect, 0L);
    }

    private User convert(UserVo userVo) {
        User user = new User();
        BeanUtils.copyProperties(userVo, user);
        return user;
    }

    private UserVo convert(User user) {
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return userVo;
    }

}
