package com.gxcy.letaotao.web.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gxcy.letaotao.web.app.entity.LTUserFollow;
import com.gxcy.letaotao.web.app.vo.LTUserQueryVo;
import com.gxcy.letaotao.web.app.vo.LTUserVo;

import java.util.List;

public interface LTWeChatUserFollowService extends BaseService<LTUserFollow> {

    IPage<LTUserVo> findUserFollowListByPage(IPage<LTUserVo> page, LTUserQueryVo userQueryVO);

    int followOrUnfollowUser(Long followerId, Long followedId);

    int findFollowerCount(Long userId);

    int findFollowingCount(Long userId);

    boolean isFollow(Long followerId, Long followedId);

    List<Long> findFollowingIdsList(Long userId);
}
