package com.gxcy.letaotao.web.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gxcy.letaotao.web.app.entity.LTUserFollow;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 关注表 Mapper 接口
 */
public interface LTUserFollowMapper extends BaseMapper<LTUserFollow> {

    @Select("select count(id) from lt_user_follow where `status` = 1 and followed_id = #{userId}")
    int findFollowerCount(Long userId); // 粉丝数量

    @Select("select count(id) from lt_user_follow where `status` = 1 and follower_id = #{userId}")
    int findFollowingCount(Long userId); // 关注数量

    @Select("select exists(select 1 from lt_user_follow where `status` = 1 and follower_id = #{followerId} " +
            "and followed_id = #{followedId})")
    boolean isFollow(Long followerId, Long followedId); // 是否关注

    @Select("select followed_id from lt_user_follow where `status` = 1 and follower_id = #{userId}")
    List<Long> findFollowingIdsList(Long userId); // 关注列表
}
