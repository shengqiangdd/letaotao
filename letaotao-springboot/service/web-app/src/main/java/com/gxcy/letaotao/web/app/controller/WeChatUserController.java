package com.gxcy.letaotao.web.app.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxcy.letaotao.common.enums.LTUserFollowStatus;
import com.gxcy.letaotao.common.utils.Result;
import com.gxcy.letaotao.web.app.dto.UserInfoDTO;
import com.gxcy.letaotao.web.app.dto.WeChatLoginDTO;
import com.gxcy.letaotao.web.app.service.LTWeChatUserFollowService;
import com.gxcy.letaotao.web.app.service.WeChatUserService;
import com.gxcy.letaotao.web.app.vo.LTUserQueryVo;
import com.gxcy.letaotao.web.app.vo.LTUserVo;
import com.gxcy.letaotao.web.app.vo.LTWechatUserFollowVo;
import com.gxcy.letaotao.web.app.vo.UserInfoVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "微信用户控制器")
@RequestMapping("/api/wx_user")
@RestController
public class WeChatUserController {

    @Resource
    private WeChatUserService weChatUserService;
    @Resource
    private LTWeChatUserFollowService userFollowService;

    @PostMapping("/login")
    @Operation(summary = "微信小程序登录")
    public void weChatLogin(@RequestBody WeChatLoginDTO weChatLoginDTO, HttpServletRequest request,
                            HttpServletResponse response) throws IOException {
        weChatUserService.login(weChatLoginDTO, request, response);
    }

    /**
     * 分页查询用户列表
     *
     * @param userQueryVO
     * @return
     */
    @GetMapping("/list/page")
    @Operation(summary = "分页查询用户列表")
    public Result<?> getUserList(LTUserQueryVo userQueryVO) {
        // 创建分页对象
        IPage<LTUserVo> page = new Page<>(userQueryVO.getPageNo(), userQueryVO.getPageSize());
        // 调用分页查询用户列表方法
        weChatUserService.findListByPage(page, userQueryVO);
        // 返回数据
        return Result.ok(page);
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @Operation(summary = "获取用户信息", description = "获取用户信息")
    @GetMapping("/{userId}")
    public Result<?> getUserInfo(@PathVariable Long userId) {
        UserInfoVo userInfo = weChatUserService.getUserInfo(userId);
        // 返回数据
        return Result.ok(userInfo).message("用户信息查询成功");
    }

    @GetMapping("/list/follow")
    @Operation(summary = "分页查询用户关注关系列表")
    public Result<?> getUserFollowList(LTUserQueryVo userQueryVO) {
        // 创建分页对象
        IPage<LTUserVo> page = new Page<>(userQueryVO.getPageNo(), userQueryVO.getPageSize());
        // 调用分页查询用户关注关系列表方法
        userFollowService.findUserFollowListByPage(page, userQueryVO);
        // 返回数据
        return Result.ok(page);
    }

    @PostMapping("/follow")
    @Operation(summary = "关注或取消关注")
    public Result<?> follow(@RequestBody LTWechatUserFollowVo userFollowVO) {
        // 调用关注或取消关注方法
        int status = userFollowService.followOrUnfollowUser(userFollowVO.getFollowerId(),
                userFollowVO.getFollowedId());
        return Result.ok(status).message(status == LTUserFollowStatus.STATUS_FOLLOW.getCode() ? "关注成功" : "取消关注成功");
    }

    @GetMapping("/followCount/{userId}")
    @Operation(summary = "获取用户关注数")
    public Result<?> getUserFollowCount(@PathVariable @Nonnull Long userId) {
        UserInfoVo userInfoVo = weChatUserService.findUserFollowCount(userId);
        return Result.ok(userInfoVo);
    }

    @PutMapping("/updateInfo")
    @Operation(summary = "更新用户信息")
    public Result<?> update(@RequestBody UserInfoDTO userInfoDTO) {
        // 调用更新用户信息方法
        boolean isSuccess = weChatUserService.updateUserInfo(userInfoDTO);
        return Result.ok().message(isSuccess ? "更新成功" : "更新操作失败");
    }

    @PostMapping("/logout")
    public Result<?> logout(HttpServletRequest request, HttpServletResponse response) {
        // 调用登出方法
        weChatUserService.logout(request, response);
        return Result.ok().message("登出成功");
    }

}
