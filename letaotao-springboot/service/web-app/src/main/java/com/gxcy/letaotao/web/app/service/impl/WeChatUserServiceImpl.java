package com.gxcy.letaotao.web.app.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.gxcy.letaotao.common.config.redis.CacheKeyConstants;
import com.gxcy.letaotao.common.config.redis.RedisService;
import com.gxcy.letaotao.common.enums.LTImagesType;
import com.gxcy.letaotao.common.enums.LTUserFollowStatus;
import com.gxcy.letaotao.common.exception.LTException;
import com.gxcy.letaotao.common.exception.WeChatApiException;
import com.gxcy.letaotao.common.utils.LTConstants;
import com.gxcy.letaotao.web.app.config.handler.WeChatLoginSuccessHandler;
import com.gxcy.letaotao.web.app.dto.UserInfoDTO;
import com.gxcy.letaotao.web.app.dto.WeChatLoginDTO;
import com.gxcy.letaotao.web.app.entity.LTUser;
import com.gxcy.letaotao.web.app.entity.LTUserFollow;
import com.gxcy.letaotao.web.app.mapper.LTUserFollowMapper;
import com.gxcy.letaotao.web.app.mapper.LTUserMapper;
import com.gxcy.letaotao.web.app.service.LTImagesService;
import com.gxcy.letaotao.web.app.service.WeChatUserService;
import com.gxcy.letaotao.web.app.utils.LoginUser;
import com.gxcy.letaotao.web.app.utils.LoginUserHolder;
import com.gxcy.letaotao.web.app.utils.WeChatProperties;
import com.gxcy.letaotao.web.app.utils.WeChatSessionResponse;
import com.gxcy.letaotao.web.app.vo.LTImagesVo;
import com.gxcy.letaotao.web.app.vo.LTUserQueryVo;
import com.gxcy.letaotao.web.app.vo.LTUserVo;
import com.gxcy.letaotao.web.app.vo.UserInfoVo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WeChatUserServiceImpl extends BaseServiceImpl<LTUserMapper, LTUser> implements WeChatUserService {

    @Resource
    private WeChatProperties weChatProperties;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Resource
    private LTUserFollowMapper userFollowMapper;
    @Resource
    private RedisService redisService;
    @Resource
    private LTImagesService ltImagesService;
    @Resource
    private WeChatLoginSuccessHandler weChatLoginSuccessHandler;
    @Resource
    private CacheManager cacheManager;

    @Override
    public IPage<LTUserVo> findListByPage(IPage<LTUserVo> page, LTUserQueryVo userQueryVO) {
        // 创建条件构造器对象
        LambdaQueryWrapper<LTUser> queryWrapper = new LambdaQueryWrapper<>();

        if (!ObjectUtils.isEmpty(userQueryVO.getLabel())) {
            queryWrapper.like(LTUser::getNickName, userQueryVO.getLabel());
            Long userId = userQueryVO.getUserId();
            IPage<LTUserVo> list = baseMapper.findListByPage(page, queryWrapper); // 分页查询用户列表
            List<LTUserVo> userList = list.getRecords();
            updateFollowCountForUsers(userList, userId);
            return list;
        }
        return null;
    }

    private void updateFollowCountForUsers(List<LTUserVo> users, Long userId) {
        // 提取所有用户的ID
        List<Long> userIds = users.stream().map(LTUserVo::getId).collect(Collectors.toList());

        LTUserFollowStatus statusFollow = LTUserFollowStatus.STATUS_FOLLOW;
        LTUserFollowStatus statusUnFollow = LTUserFollowStatus.STATUS_UNFOLLOW;
        if (!userIds.isEmpty()) {
            // 获取所有用户的粉丝计数(被哪些人关注)
            Map<Long, Integer> followerCountMap = userFollowMapper.selectList(
                            new LambdaQueryWrapper<LTUserFollow>()
                                    .in(LTUserFollow::getFollowedId, userIds)
                                    .eq(LTUserFollow::getStatus, statusFollow))
                    .stream()
                    .collect(Collectors.groupingBy(LTUserFollow::getFollowedId, Collectors.reducing(0, e -> 1, Integer::sum)));

            // 获取所有用户的关注计数(关注了哪些人)
            Map<Long, Integer> followingCountMap = userFollowMapper.selectList(
                            new LambdaQueryWrapper<LTUserFollow>()
                                    .in(LTUserFollow::getFollowerId, userIds)
                                    .eq(LTUserFollow::getStatus, statusFollow))
                    .stream()
                    .collect(Collectors.groupingBy(LTUserFollow::getFollowerId, Collectors.reducing(0, e -> 1, Integer::sum)));

            // 如果userId不是null，则查找此用户关注了哪些人
            final Set<Long> followedUserIds;
            if (userId != null) {
                followedUserIds = userFollowMapper.selectList(
                                new LambdaQueryWrapper<LTUserFollow>()
                                        .eq(LTUserFollow::getFollowerId, userId)
                                        .eq(LTUserFollow::getStatus, statusFollow))
                        .stream()
                        .map(LTUserFollow::getFollowedId)
                        .collect(Collectors.toSet());
            } else {
                // userId为null时，采用空的不可变集合
                followedUserIds = Collections.emptySet();
            }

            // 更新用户对象的统计信息
            users.forEach(user -> {
                user.setFollowerCount(followerCountMap.getOrDefault(user.getId(), 0)); // 粉丝数量
                user.setFollowingCount(followingCountMap.getOrDefault(user.getId(), 0)); // 关注数量
                user.setIsFollowed(followedUserIds.contains(user.getId()) ? statusFollow : statusUnFollow); // 是否已关注
            });
        }

    }

    @Override
    public String getOpenIdByCode(String code) {
        // 请确保 code 非空
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("code 是必需的");
        }

        String appId = weChatProperties.getAppid(); // 微信小程序的 appId
        String appSecret = weChatProperties.getSecret(); // 微信小程序的 appSecret
        String weChatApiUrl = "https://api.weixin.qq.com/sns/jscode2session"; // 微信登录凭证校验的 API 地址
        // 构建微信登录凭证校验的 URL
        String url = String.format("%s?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                weChatApiUrl, appId, appSecret, code);

        // 发送请求并接收响应
        try {
            String response = restTemplate.getForObject(url, String.class); // 使用RestTemplate发送GET请求
            if (response == null || response.isEmpty()) {
                throw new WeChatApiException("从微信获取 session_key 失败，响应为空");
            }

            // 解析响应为WeChatSessionResponse对象
            WeChatSessionResponse sessionResponse = JSON.parseObject(response, WeChatSessionResponse.class);

            log.debug("微信认证：{}", response);
            // 解析响应
            return sessionResponse.getOpenid();
        } catch (RestClientException e) {
            // 当HTTP客户端调用出现异常时（如连接失败、超时等）
            throw new WeChatApiException("调用微信API时发生错误", e);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LTUserVo registerNewUser(UserInfoDTO userInfo, String openId) {
        LTUserVo userVo = buildUserFromUserInfo(userInfo, openId); // 构建用户对象
        boolean rowsAffected = this.add(userVo);
        if (!rowsAffected) {
            throw new IllegalStateException("注册新用户失败");
        }
        return userVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserInfo(UserInfoDTO userInfoDTO) { // 更新用户信息
        // 查询id=userId的用户
        LTUser user = baseMapper.selectById(userInfoDTO.getUserId());
        if (user == null) {
            // 用户不存在，无法更新
            return false;
        }

        return this.update(userInfoDTO);
    }

    @Override
    @Cacheable(value = CacheKeyConstants.APP_USER, key = "#result.id", unless = "#result == null")
    public LTUserVo findUserByOpenId(String openId) {
        return baseMapper.findUserByOpenId(openId);
    }

    @Override
    @Cacheable(value = CacheKeyConstants.APP_USER, key = "#id", unless = "#result == null")
    public LTUserVo findUserById(Long id) {
        return baseMapper.findUserById(id);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        // 获取token
        String token = request.getHeader(LTConstants.TOKEN);
        if (ObjectUtils.isEmpty(token)) {
            token = request.getParameter(LTConstants.TOKEN);
        }
        // 从ThreadLocal中获取用户信息
        LoginUser loginUser = LoginUserHolder.getLoginUser();

        if (loginUser != null) {
            // 清空redis里面的token
            String key = CacheKeyConstants.ADMIN_TOKEN_PREFIX + token;
            redisService.del(key);
        }
    }

    @Override
    public LTUserVo getCurrentUser() {
        LoginUser loginUser = LoginUserHolder.getLoginUser();
        Long userId = LoginUserHolder.getLoginUser().getUserId();
        return baseMapper.findUserById(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(LTUserVo userVo) {
        LTUser ltUser = this.convert(userVo);
        if (this.save(ltUser)) {
            this.cacheUserById(ltUser.getId());
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheKeyConstants.APP_USER, key = "#userVo.id")
    public boolean update(LTUserVo userVo) {
        return this.updateById(this.convert(userVo));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheKeyConstants.APP_USER, key = "#userInfoDTO.userId")
    public boolean update(UserInfoDTO userInfoDTO) {
        LTUser ltUser = this.convert(userInfoDTO);
        ltUser.setId(userInfoDTO.getUserId());
        return this.updateById(ltUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheKeyConstants.APP_USER, key = "#id")
    public boolean deleteById(Long id) {
        // 查询
        LTUser user = baseMapper.selectById(id);
        // 删除用户
        if (baseMapper.deleteById(id) > 0) {
            // 判断有无头像，有则删除
            if (user != null && !ObjectUtils.isEmpty(user.getAvatar())) {
                List<LTImagesVo> imagesList = ltImagesService.getImagesList(Math.toIntExact(user.getId()), LTImagesType.USER);
                for (LTImagesVo imagesVo : imagesList) {
                    ltImagesService.deleteById(imagesVo.getId());
                }
            }
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void login(WeChatLoginDTO weChatLoginDTO, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("微信登录：{}", weChatLoginDTO.toString());
        // 根据code 获取 openId
        String code = weChatLoginDTO.getCode();
        String openId = this.getOpenIdByCode(code);
        // 根据 openId 获取用户信息
        LTUserVo user = baseMapper.findUserByOpenId(openId);

        // 如果数据库中没有对应 openId 的用户，则注册用户
        if (user == null) {
            user = this.registerNewUser(weChatLoginDTO.getUserInfo(), openId);
            user.setIsNewUser(true);
        } else {
            if (Objects.equals(user.getNickName(), weChatProperties.getDefaultNickName()) ||
                    Objects.equals(user.getAvatar(), weChatProperties.getDefaultAvatarUrl())) {
                user.setIsNewUser(true);
            }
        }

        // 调用登录成功处理器
        weChatLoginSuccessHandler.onAuthenticationSuccess(request, response, user);
    }

    @Override
    public UserInfoVo getUserInfo(Long userId) {
        // 获取用户信息
        LTUserVo user = baseMapper.findUserById(userId);
        // 判断用户是否存在
        if (user == null) {
            throw new LTException("用户信息查询失败");
        }
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(user, userInfoVo);
        userInfoVo.setFollowerCount(userFollowMapper.findFollowerCount(userId)); // 获取粉丝数量
        userInfoVo.setFollowingCount(userFollowMapper.findFollowingCount(userId)); // 获取关注数量
        return userInfoVo;
    }

    @Override
    public UserInfoVo findUserFollowCount(Long userId) {
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setId(userId);
        int followerCount = userFollowMapper.findFollowerCount(userId); // 获取粉丝数量
        int followingCount = userFollowMapper.findFollowingCount(userId); // 获取关注数量
        userInfoVo.setFollowerCount(followerCount); // 设置粉丝数量
        userInfoVo.setFollowingCount(followingCount); // 设置关注数量
        return userInfoVo;
    }

    @CachePut(value = CacheKeyConstants.APP_USER, key = "#result.id", unless = "#result == null")
    public LTUserVo cacheUserById(Long id) {
        return this.convert(this.getById(id));
    }

    private LTUser convert(LTUserVo userVo) {
        LTUser ltUser = new LTUser();
        BeanUtils.copyProperties(userVo, ltUser);
        return ltUser;
    }

    private LTUserVo convert(LTUser ltUser) {
        LTUserVo ltUserVo = new LTUserVo();
        BeanUtils.copyProperties(ltUser, ltUserVo);
        return ltUserVo;
    }

    private LTUser convert(UserInfoDTO userInfo) {
        LTUser ltUser = new LTUser();
        BeanUtils.copyProperties(userInfo, ltUser);
        return ltUser;
    }

    private LTUserVo buildUserFromUserInfo(UserInfoDTO userInfo, String openId) { // 从用户信息构建用户对象
        LTUserVo user = new LTUserVo();
        user.setUsername(openId);
        user.setAvatar(userInfo.getAvatarUrl());
        user.setGender(userInfo.getGender());
        user.setNickName(userInfo.getNickName());
        user.setOpenId(openId);
        user.setPassword(bCryptPasswordEncoder.encode(UUID.randomUUID().toString()));
        return user;
    }
}
