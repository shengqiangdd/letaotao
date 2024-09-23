package com.gxcy.letaotao.web.app.config.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gxcy.letaotao.common.config.redis.CacheKeyConstants;
import com.gxcy.letaotao.common.config.redis.RedisService;
import com.gxcy.letaotao.common.enums.LTUserFollowStatus;
import com.gxcy.letaotao.common.utils.JwtUtils;
import com.gxcy.letaotao.common.utils.Result;
import com.gxcy.letaotao.common.utils.ResultCode;
import com.gxcy.letaotao.web.app.entity.LTUserFollow;
import com.gxcy.letaotao.web.app.service.LTWeChatUserFollowService;
import com.gxcy.letaotao.web.app.vo.LTUserVo;
import com.gxcy.letaotao.web.app.vo.LoginResult;
import com.gxcy.letaotao.web.app.vo.UserInfoVo;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 登录认证成功处理类
 */
@Component
public class WeChatLoginSuccessHandler {

    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private RedisService redisService;
    @Resource
    private LTWeChatUserFollowService userFollowService;
    @Resource
    private ObjectMapper objectMapper;

    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, LTUserVo user)
            throws IOException {
        // 设置客户端的响应内容类型
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // 设置token过期时间为30天
        long thirtyDaysInMillis = jwtUtils.getExpirationDurationInMillis(TimeUnit.DAYS.toDays(30));
        String token = jwtUtils.generateTokenWithExpiration(user, thirtyDaysInMillis);

        setFollowCount(user);
        UserInfoVo userInfo = new UserInfoVo();
        BeanUtils.copyProperties(user, userInfo);
        // 创建登录对象结果
        LoginResult loginResult = new LoginResult(
                user.getId(), ResultCode.SUCCESS, token, userInfo,
                System.currentTimeMillis() + thirtyDaysInMillis);
        // 把生成的token存到redis
        String tokenKey = CacheKeyConstants.APP_TOKEN_PREFIX + token;
        redisService.set(tokenKey, token, thirtyDaysInMillis / 1000);
        // 使用统一的响应格式类封装响应
        String result = objectMapper.writeValueAsString(Result.ok(loginResult));
        // 响应给客户端
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(result.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }

    private void setFollowCount(LTUserVo user) {
        // 获取当前用户的粉丝计数
        Map<Long, Integer> followerCountMap = userFollowService.list(
                        new LambdaQueryWrapper<LTUserFollow>()
                                .eq(LTUserFollow::getFollowedId, user.getId())
                                .eq(LTUserFollow::getStatus, LTUserFollowStatus.STATUS_FOLLOW))
                .stream()
                .collect(Collectors.groupingBy(LTUserFollow::getFollowedId, Collectors.reducing(0, e -> 1, Integer::sum)));

        // 获取当前用户的关注计数
        Map<Long, Integer> followingCountMap = userFollowService.list(
                        new LambdaQueryWrapper<LTUserFollow>()
                                .eq(LTUserFollow::getFollowerId, user.getId())
                                .eq(LTUserFollow::getStatus, LTUserFollowStatus.STATUS_FOLLOW))
                .stream()
                .collect(Collectors.groupingBy(LTUserFollow::getFollowerId, Collectors.reducing(0, e -> 1, Integer::sum)));

        // 更新用户对象的统计信息
        user.setFollowerCount(followerCountMap.getOrDefault(user.getId(), 0));
        user.setFollowingCount(followingCountMap.getOrDefault(user.getId(), 0));
    }
}
