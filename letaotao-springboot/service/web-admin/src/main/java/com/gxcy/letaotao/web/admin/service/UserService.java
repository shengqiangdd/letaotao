package com.gxcy.letaotao.web.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gxcy.letaotao.web.admin.entity.User;
import com.gxcy.letaotao.web.admin.entity.UserInfo;
import com.gxcy.letaotao.web.admin.vo.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;


public interface UserService extends BaseService<User> {

    /**
     * 根据用户名查询用户信息
     *
     * @param username
     * @return
     */
    UserVo findUserByUserName(String username);

    /**
     * 分页查询用户信息
     *
     * @param page
     * @param userQueryVo
     * @return
     */
    IPage<UserVo> findUserListByPage(IPage<UserVo> page, UserQueryVo userQueryVo);

    /**
     * 删除用户信息
     *
     * @return
     */
    boolean deleteById(Long id);

    /**
     * 分配角色
     *
     * @param userId
     * @param roleIds
     * @return
     */
    boolean saveUserRole(Long userId, List<Long> roleIds);

    UserVo findUserById(Long id);

    boolean add(UserVo userVo);

    boolean update(UserVo userVo);

    void logout(HttpServletRequest request, HttpServletResponse response);

    void login(LoginVo user, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;

    TokenVo refreshToken(HttpServletRequest request);

    UserInfo getUserInfo();

    List<RouterVo> getMenuList();
}
