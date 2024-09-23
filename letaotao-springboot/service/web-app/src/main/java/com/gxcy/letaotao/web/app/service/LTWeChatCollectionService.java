package com.gxcy.letaotao.web.app.service;

import com.gxcy.letaotao.common.entity.LTCollection;
import com.gxcy.letaotao.common.enums.LTCollectionTargetType;
import com.gxcy.letaotao.web.app.vo.LTWechatCollectionQueryVo;
import com.gxcy.letaotao.web.app.vo.LTWechatCollectionVo;

import java.util.List;

/**
 * 收藏表 服务类
 */
public interface LTWeChatCollectionService extends BaseService<LTCollection> {

    /**
     * 添加或取消收藏
     *
     * @param LTCollection
     * @return
     */
    int addOrUpdate(LTWechatCollectionVo wechatCollectionVo);

    List<LTWechatCollectionVo> getProductList(LTWechatCollectionQueryVo ltWechatCollectionQueryVO);

    List<LTWechatCollectionVo> getPostList(LTWechatCollectionQueryVo ltWechatCollectionQueryVO);

    int findCollectionCount(Integer targetId, LTCollectionTargetType targetType);

    boolean isCollection(Integer targetId, LTCollectionTargetType targetType, Long userId);

    LTWechatCollectionVo findByUserIdAndTargetIdAndTargetType(Long userId, Integer targetId, LTCollectionTargetType targetType);

    void deleteByTargetIdAndType(Integer targetId, LTCollectionTargetType targetType);

    boolean add(LTWechatCollectionVo collectionVo);

    boolean update(LTWechatCollectionVo collectionVo);

    boolean deleteById(Integer id);

    void deleteByIds(List<Integer> ids);
}
