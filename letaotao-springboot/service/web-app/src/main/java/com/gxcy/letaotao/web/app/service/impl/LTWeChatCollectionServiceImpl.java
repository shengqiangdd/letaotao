package com.gxcy.letaotao.web.app.service.impl;

import com.gxcy.letaotao.common.config.redis.CacheKeyConstants;
import com.gxcy.letaotao.common.entity.LTCollection;
import com.gxcy.letaotao.common.enums.BooleanStatus;
import com.gxcy.letaotao.common.enums.LTCollectionTargetType;
import com.gxcy.letaotao.common.enums.LTImagesType;
import com.gxcy.letaotao.common.exception.LTException;
import com.gxcy.letaotao.web.app.mapper.LTCollectionMapper;
import com.gxcy.letaotao.web.app.mapper.LTImagesMapper;
import com.gxcy.letaotao.web.app.service.LTWeChatCollectionService;
import com.gxcy.letaotao.web.app.service.WeChatUserService;
import com.gxcy.letaotao.web.app.vo.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 收藏表 服务实现类
 */
@Service
@Slf4j
public class LTWeChatCollectionServiceImpl extends BaseServiceImpl<LTCollectionMapper, LTCollection> implements LTWeChatCollectionService {

    @Resource
    private LTImagesMapper ltImagesMapper;
    @Resource
    private CacheManager cacheManager;
    @Resource
    private WeChatUserService wechatUserService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addOrUpdate(LTWechatCollectionVo wechatCollectionVo) {
        // 根据 targetId、userId、targetType 查询收藏记录
        LTWechatCollectionVo existCollection = baseMapper.findByUserIdAndTargetIdAndTargetType(wechatCollectionVo.getUserId(),
                wechatCollectionVo.getTargetId(), wechatCollectionVo.getTargetType());

        BooleanStatus isActive;
        try {
            if (existCollection != null) {
                // 如果收藏记录存在，则更新状态，并返回状态
                isActive = existCollection.getIsActive() == BooleanStatus.TRUE ? BooleanStatus.FALSE : BooleanStatus.TRUE;
                existCollection.setIsActive(isActive);
                this.update(existCollection);
            } else {
                // 如果收藏记录不存在，则新增记录，并返回状态
                isActive = BooleanStatus.TRUE; // 新记录默认为激活状态
                wechatCollectionVo.setIsActive(isActive);
                this.add(wechatCollectionVo);
            }
            this.deleteCacheCollectionByUserIdAndTargetIdAndTargetType(wechatCollectionVo.getUserId()
                    , wechatCollectionVo.getTargetId(), wechatCollectionVo.getTargetType());
            return isActive == BooleanStatus.TRUE ? BooleanStatus.TRUE.getCode() : BooleanStatus.FALSE.getCode();
        } catch (Exception e) {
            log.debug("收藏异常", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<LTWechatCollectionVo> getProductList(LTWechatCollectionQueryVo ltWechatCollectionVO) {
        // 查询收藏商品
        List<LTWechatCollectionVo> collectionList = baseMapper.findProductList(ltWechatCollectionVO);
        for (LTWechatCollectionVo collection : collectionList) {
            LTWechatProductVo product = collection.getProduct();
            // 查询商品图片
            product.setImages(ltImagesMapper.getImagesList(product.getId(), LTImagesType.PRODUCT));
        }
        return collectionList;
    }

    @Override
    public List<LTWechatCollectionVo> getPostList(LTWechatCollectionQueryVo ltWechatCollectionVO) {
        // 查询收藏帖子
        List<LTWechatCollectionVo> collectionList = baseMapper.findPostList(ltWechatCollectionVO);
        for (LTWechatCollectionVo collection : collectionList) {
            LTWechatPostVo post = collection.getPost();
            // 查询帖子图片
            post.setImages(ltImagesMapper.getImagesList(post.getId(), LTImagesType.POST));
        }
        return collectionList;
    }

    @Override
    @Cacheable(value = CacheKeyConstants.COLLECTION, key = "'count_'+#targetId + '_' + #targetType.code", unless = "#result == null")
    public int findCollectionCount(Integer targetId, LTCollectionTargetType targetType) {
        // 根据 targetId、targetType 查询收藏数量
        return baseMapper.findCollectionCount(targetId, targetType);
    }

    @Override
    @Cacheable(value = CacheKeyConstants.COLLECTION, key = "'bool_'+#userId + '_' + #targetId + '_' + #targetType.code", unless = "#result == null")
    public boolean isCollection(Integer targetId, LTCollectionTargetType targetType, Long userId) {
        // 根据 targetId、targetType、userId 查询是否已收藏
        return baseMapper.isCollection(targetId, targetType, userId);
    }

    @Override
    @Cacheable(value = CacheKeyConstants.COLLECTION, key = "#userId + '_' + #targetId + '_' + #targetType.code", unless = "#result == null")
    public LTWechatCollectionVo findByUserIdAndTargetIdAndTargetType(Long userId, Integer targetId, LTCollectionTargetType targetType) {
        return baseMapper.findByUserIdAndTargetIdAndTargetType(userId, targetId, targetType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByTargetIdAndType(Integer targetId, LTCollectionTargetType targetType) {
        // 根据 targetId、targetType 删除收藏记录
        baseMapper.deleteByTargetId(targetId, targetType);
        LTUserVo currentUser = wechatUserService.getCurrentUser();
        if (Objects.nonNull(currentUser)) {
            // 删除缓存
            deleteCacheCollectionByUserIdAndTargetIdAndTargetType(currentUser.getId(), targetId, targetType);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(LTWechatCollectionVo collectionVo) {
        LTCollection ltCollection = this.convert(collectionVo);
        if (this.save(ltCollection)) {
            this.cacheCollectionById(ltCollection.getId());
            return true;
        }
        return false;
    }

    @Override
    @CacheEvict(value = CacheKeyConstants.COLLECTION, key = "#collectionVo.id")
    @Transactional(rollbackFor = Exception.class)
    public boolean update(LTWechatCollectionVo collectionVo) {
        if (this.updateById(this.convert(collectionVo))) {
            deleteCacheCollectionByUserIdAndTargetIdAndTargetType(collectionVo.getUserId(),
                    collectionVo.getTargetId(), collectionVo.getTargetType());
            return true;
        }
        return false;
    }

    @Override
    @CacheEvict(value = CacheKeyConstants.COLLECTION, key = "#id")
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Integer id) {
        LTCollection ltCollection = this.getById(id);
        if (this.removeById(id)) {
            deleteCacheCollectionByUserIdAndTargetIdAndTargetType(ltCollection.getUserId(),
                    ltCollection.getTargetId(), ltCollection.getTargetType());
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<Integer> ids) {
        if (ids.isEmpty()) {
            throw new LTException("请选择要取消收藏的数据");
        }
        List<LTCollection> ltCollections = baseMapper.selectBatchIds(ids);
        if (this.removeByIds(ids)) {
            ltCollections.forEach(ltCollection -> deleteCacheCollectionByUserIdAndTargetIdAndTargetType(ltCollection.getUserId(),
                    ltCollection.getTargetId(), ltCollection.getTargetType()));
        }
    }

    @CachePut(value = CacheKeyConstants.COLLECTION, key = "#result.id", unless = "#result == null")
    public LTWechatCollectionVo cacheCollectionById(Integer id) {
        return this.convert(this.getById(id));
    }

    private LTWechatCollectionVo convert(LTCollection collection) {
        LTWechatCollectionVo collectionVo = new LTWechatCollectionVo();
        BeanUtils.copyProperties(collection, collectionVo);
        return collectionVo;
    }

    private LTCollection convert(LTWechatCollectionVo collectionVo) {
        LTCollection collection = new LTCollection();
        BeanUtils.copyProperties(collectionVo, collection);
        return collection;
    }

    private void deleteCacheCollectionByUserIdAndTargetIdAndTargetType(Long userId, Integer targetId, LTCollectionTargetType targetType) {
        Objects.requireNonNull(this.cacheManager.getCache(CacheKeyConstants.COLLECTION))
                .evict("count_" + targetId + "_" + targetType.getCode());
        Objects.requireNonNull(this.cacheManager.getCache(CacheKeyConstants.COLLECTION))
                .evict("bool_" + userId + "_" + targetId + "_" + targetType.getCode());
        Objects.requireNonNull(this.cacheManager.getCache(CacheKeyConstants.COLLECTION))
                .evict(userId + "_" + targetId + "_" + targetType.getCode());
        Objects.requireNonNull(this.cacheManager.getCache(CacheKeyConstants.POST)).evict(targetId); // 清除帖子缓存
        Objects.requireNonNull(cacheManager.getCache(CacheKeyConstants.PRODUCT)).evict(targetId); // 清除商品缓存
    }
}
