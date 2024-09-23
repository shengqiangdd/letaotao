package com.gxcy.letaotao.web.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gxcy.letaotao.common.entity.LTLike;
import com.gxcy.letaotao.common.enums.LTLikeTargetType;
import com.gxcy.letaotao.web.app.dto.LTLikeDTO;

/**
 * 点赞表 服务类
 */
public interface LTWeChatLikeService extends IService<LTLike> {

    /**
     * 点赞或取消点赞
     *
     * @param ltLike
     * @return
     */
    int addOrUpdate(LTLikeDTO ltLike);

    int findLikeCount(Integer targetId, LTLikeTargetType targetType);

    /**
     * 查询是否点赞
     *
     * @param targetId
     * @param targetType
     * @param userId
     * @return
     */
    boolean isLike(Integer targetId, LTLikeTargetType targetType, Long userId);

    void deleteByTargetId(Integer targetId, LTLikeTargetType targetType);

}
