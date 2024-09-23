package com.gxcy.letaotao.web.app.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gxcy.letaotao.common.enums.LTUserFollowStatus;
import com.gxcy.letaotao.common.enums.UserFollowType;
import com.gxcy.letaotao.web.app.entity.LTUser;
import com.gxcy.letaotao.web.app.entity.LTUserFollow;
import com.gxcy.letaotao.web.app.mapper.LTUserFollowMapper;
import com.gxcy.letaotao.web.app.mapper.LTUserMapper;
import com.gxcy.letaotao.web.app.service.LTWeChatUserFollowService;
import com.gxcy.letaotao.web.app.vo.LTUserQueryVo;
import com.gxcy.letaotao.web.app.vo.LTUserVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LTWeChatUserFollowServiceImpl extends BaseServiceImpl<LTUserFollowMapper, LTUserFollow> implements LTWeChatUserFollowService {

    @Resource
    private LTUserMapper userMapper;

    @Override
    public IPage<LTUserVo> findUserFollowListByPage(IPage<LTUserVo> page, LTUserQueryVo userQueryVO) {
        LambdaQueryWrapper<LTUserFollow> queryWrapper = new LambdaQueryWrapper<>();
        UserFollowType followType = userQueryVO.getFollowType();
        // 确保查询用户ID不为空
        if (!ObjectUtils.isEmpty(userQueryVO.getUserId())) {
            // 根据followType判断查询条件 默认为粉丝
            if (UserFollowType.FOLLOWERS.equals(followType)) {
                queryWrapper.eq(LTUserFollow::getFollowedId, userQueryVO.getUserId());
            } else {
                queryWrapper.eq(LTUserFollow::getFollowerId, userQueryVO.getUserId());
            }
            queryWrapper.eq(LTUserFollow::getStatus, LTUserFollowStatus.STATUS_FOLLOW);
        }

        Set<Long> userIds = baseMapper.selectList(queryWrapper).stream()
                .map(follow -> UserFollowType.FOLLOWERS.equals(followType) ? follow.getFollowerId() : follow.getFollowedId())
                .collect(Collectors.toSet()); // 将用户关注者或粉丝的ID集合转换为Set

        LambdaQueryWrapper<LTUser> userWrapper = new LambdaQueryWrapper<>();
        if (userIds.isEmpty()) {
            userWrapper.in(LTUser::getId, -1);
        } else {
            userWrapper.in(LTUser::getId, userIds);
        }

        IPage<LTUserVo> userPage = userMapper.findListByPage(page, userWrapper); // 根据条件查询用户列表
        updateFollowCountForUsers(userPage.getRecords(), userQueryVO.getUserId()); // 更新用户的关注和粉丝数量

        return userPage;
    }

    protected void updateFollowCountForUsers(List<LTUserVo> users, Long userId) {
        // 提取所有用户的ID
        List<Long> userIds = users.stream().map(LTUserVo::getId).collect(Collectors.toList());

        if (!userIds.isEmpty()) {
            // 获取所有用户的粉丝计数(被哪些人关注)
            Map<Long, Integer> followerCountMap = baseMapper.selectList(
                            new LambdaQueryWrapper<LTUserFollow>()
                                    .in(LTUserFollow::getFollowedId, userIds)
                                    .eq(LTUserFollow::getStatus, LTUserFollowStatus.STATUS_FOLLOW))
                    .stream()
                    .collect(Collectors.groupingBy(LTUserFollow::getFollowedId, Collectors.reducing(0, e -> 1, Integer::sum)));

            // 获取所有用户的关注计数(关注了哪些人)
            Map<Long, Integer> followingCountMap = baseMapper.selectList(
                            new LambdaQueryWrapper<LTUserFollow>()
                                    .in(LTUserFollow::getFollowerId, userIds)
                                    .eq(LTUserFollow::getStatus, LTUserFollowStatus.STATUS_FOLLOW))
                    .stream()
                    .collect(Collectors.groupingBy(LTUserFollow::getFollowerId, Collectors.reducing(0, e -> 1, Integer::sum)));

            // 如果userId不是null，则查找此用户关注了哪些人
            final Set<Long> followedUserIds;
            if (userId != null) {
                followedUserIds = baseMapper.selectList(
                                new LambdaQueryWrapper<LTUserFollow>()
                                        .eq(LTUserFollow::getFollowerId, userId)
                                        .eq(LTUserFollow::getStatus, LTUserFollowStatus.STATUS_FOLLOW))
                        .stream()
                        .map(LTUserFollow::getFollowedId)
                        .collect(Collectors.toSet());
            } else {
                // userId为null时，采用空的不可变集合
                followedUserIds = Collections.emptySet();
            }

            // 更新用户对象的统计信息
            users.forEach(user -> {
                user.setFollowerCount(followerCountMap.getOrDefault(user.getId(), 0)); // 获取粉丝数量
                user.setFollowingCount(followingCountMap.getOrDefault(user.getId(), 0)); // 获取关注数量
                user.setIsFollowed(followedUserIds.contains(user.getId()) ? LTUserFollowStatus.STATUS_FOLLOW : LTUserFollowStatus.STATUS_UNFOLLOW); // 判断是否被当前用户关注
            });
        }
    }

    @Override
    public int followOrUnfollowUser(Long followerId, Long followedId) {
        LambdaQueryWrapper<LTUserFollow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LTUserFollow::getFollowerId, followerId)
                .eq(LTUserFollow::getFollowedId, followedId);
        LTUserFollow userFollow = this.getOne(queryWrapper); // 判断是否已存在 已存在则切换状态，不存在则新增
        if (userFollow == null) {
            LTUserFollow uFollow = new LTUserFollow(null, followerId, followedId, LTUserFollowStatus.STATUS_FOLLOW,
                    LocalDateTime.now(), null);
            boolean save = this.save(uFollow);
            return save ? uFollow.getStatus().getCode() : 0;
        } else {
            userFollow.setStatus(LTUserFollowStatus.toggle(userFollow.getStatus())); // 切换状态
            userFollow.setUpdateTime(LocalDateTime.now());
            boolean update = this.updateById(userFollow);
            return update ? userFollow.getStatus().getCode() : 0;
        }
    }

    @Override
    public int findFollowerCount(Long userId) {
        // 通过用户ID查询粉丝数量
        return baseMapper.findFollowerCount(userId);
    }

    @Override
    public int findFollowingCount(Long userId) {
        // 通过用户ID查询关注数量
        return baseMapper.findFollowingCount(userId);
    }

    @Override
    public boolean isFollow(Long followerId, Long followedId) {
        // 通过用户ID和被关注者ID查询是否已关注
        return baseMapper.isFollow(followerId, followedId);
    }

    @Override
    public List<Long> findFollowingIdsList(Long userId) {
        // 通过用户ID查询关注列表
        return baseMapper.findFollowingIdsList(userId);
    }
}
