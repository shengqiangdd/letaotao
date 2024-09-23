package com.gxcy.letaotao.web.app.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.gxcy.letaotao.common.config.redis.CacheKeyConstants;
import com.gxcy.letaotao.common.entity.LTProduct;
import com.gxcy.letaotao.common.enums.*;
import com.gxcy.letaotao.common.exception.LTException;
import com.gxcy.letaotao.web.app.dto.LTWeChatProductDTO;
import com.gxcy.letaotao.web.app.mapper.LTProductMapper;
import com.gxcy.letaotao.web.app.service.*;
import com.gxcy.letaotao.web.app.vo.LTImagesVo;
import com.gxcy.letaotao.web.app.vo.LTUserVo;
import com.gxcy.letaotao.web.app.vo.LTWechatProductQueryVo;
import com.gxcy.letaotao.web.app.vo.LTWechatProductVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class LTWeChatProductServiceImpl extends BaseServiceImpl<LTProductMapper, LTProduct> implements LTWeChatProductService {

    @Resource
    private LTWeChatCollectionService ltCollectionService;
    @Resource
    private LTWeChatCommentService ltCommentService;
    @Resource
    private WeChatUserService userService;
    @Resource
    private LTImagesService ltImagesService;
    @Resource
    private LTCategoryService ltCategoryService;
    @Resource
    private LTWeChatUserFollowService ltUserFollowService;
    @Resource
    private LTWeChatChatRelationService chatRelationService;
    @Resource
    private LTWeChatLikeService ltLikeService;
    @Resource
    private CacheManager cacheManager;

    @Override
    public IPage<LTWechatProductVo> findListByPage(IPage<LTWechatProductVo> page, LTWechatProductQueryVo productQueryVo) {
        // 创建条件构造器对象
        QueryWrapper<LTWechatProductQueryVo> queryWrapper = new QueryWrapper<>();

        // 商品名
        queryWrapper.like(!ObjectUtils.isEmpty(productQueryVo.getName()),
                "p.`name`", productQueryVo.getName());
        // 商品描述
        queryWrapper.like(!ObjectUtils.isEmpty(productQueryVo.getDescription()),
                "p.description", productQueryVo.getDescription());

        queryWrapper.eq(!ObjectUtils.isEmpty(productQueryVo.getPublisherId()),
                "p.publisher_id", productQueryVo.getPublisherId());

        // 发布者
        queryWrapper.eq(!ObjectUtils.isEmpty(productQueryVo.getUserId()) && productQueryVo.getUserId() > 0,
                "p.publisher_id", productQueryVo.getUserId());
        // 商品状态
        queryWrapper.eq(!ObjectUtils.isEmpty(productQueryVo.getStatus()),
                "p.`status`", productQueryVo.getStatus());
        if (ObjectUtils.isEmpty(productQueryVo.getStatus())) {
            queryWrapper.eq("p.`status`", LTProductStatus.STATUS_ON_SHELF);
        }
        queryWrapper.eq("p.is_lock", BooleanStatus.FALSE); // 未锁定
        if (!ObjectUtils.isEmpty(productQueryVo.getTabValue())) {
            Integer tabValue = productQueryVo.getTabValue();
            if (tabValue == 0) {
                queryWrapper.orderByDesc("p.is_recommended", "p.publish_time"); // 按照推荐、发布时间降序排序
            } else if (tabValue == 1) {
                queryWrapper.orderByDesc("p.publish_time"); // 按照发布时间降序排序
            }
        }
        if (!ObjectUtils.isEmpty(productQueryVo.getLabel())) {
            String label = productQueryVo.getLabel();
            queryWrapper.and(qw -> {
                qw.like("p.`name`", label) // 商品名、商品描述、分类名
                        .or(i -> i.like("p.description", label)
                                .or(j -> j.like("c.`name`", label)));
            });
        }
        String productValue = productQueryVo.getProductValue();
        String sortValue = productQueryVo.getSortValue();
        String conditionValue = productQueryVo.getConditionValue();
        if (!ObjectUtils.isEmpty(conditionValue) && !conditionValue.equals("all")) {
            queryWrapper.eq("p.`condition`", conditionValue); // 商品成色
        }
        if (!ObjectUtils.isEmpty(productValue) && !ObjectUtils.isEmpty(sortValue)) {
            if (productValue.equals("new") && sortValue.equals("price")) {
                queryWrapper.orderByDesc("p.publish_time", "p.price"); // 按照发布时间、价格降序排序
            } else if (productValue.equals("new")) {
                queryWrapper.orderByDesc("p.publish_time"); // 按照发布时间降序排序
            } else if (productValue.equals("price")) {
                queryWrapper.orderByDesc("p.price"); // 按照价格降序排序
            }
        }

        IPage<LTWechatProductVo> list = baseMapper.findListByPage(page, queryWrapper); // 查询商品列表
        List<LTWechatProductVo> ltProducts = list.getRecords();
        for (LTWechatProductVo ltProduct : ltProducts) {
            // 查询和商品关联的图片
            ltProduct.setImages(ltImagesService.getImagesList(ltProduct.getId(), LTImagesType.PRODUCT));
            // 获取商品收藏、留言数量
            ltProduct.setCollectCount(ltCollectionService.findCollectionCount(ltProduct.getId(), LTCollectionTargetType.PRODUCT));
            ltProduct.setCommentCount(ltCommentService.findCommentCount(ltProduct.getId(), LTCommentType.LEAVE_MESSAGE));
        }
        // 查询并返回数据
        return list;
    }

    @Override
    public IPage<LTWechatProductVo> findListPageByUserId(IPage<LTWechatProductVo> page, LTWechatProductQueryVo productQueryVo) {
        return this.findListByPage(page, productQueryVo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int publish(LTWeChatProductDTO productDTO) {
        return this.handleProductOperation(productDTO, LTProductStatus.STATUS_ON_SHELF);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(LTWeChatProductDTO productDTO) {
       return this.handleProductOperation(productDTO, LTProductStatus.STATUS_DRAFT);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean withdraw(LTWechatProductVo ltProductVo) {
        return this.handleProductStatus(ltProductVo, LTProductStatus.STATUS_OFF_SHELF);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean relist(LTWechatProductVo ltProductVo) {
        return this.handleProductStatus(ltProductVo, LTProductStatus.STATUS_ON_SHELF);
    }

    @Override
    public LTWechatProductVo getProductRelationImagesById(Integer id) {
        LTProduct product = getById(id);
        LTWechatProductVo productVo = new LTWechatProductVo();
        BeanUtils.copyProperties(product, productVo);
        LTUserVo userVo = userService.findUserById(productVo.getPublisherId()); // 获取发布者信息
        productVo.setAvatar(userVo.getAvatar());
        productVo.setNickName(userVo.getNickName());
        productVo.setCategoryName(ltCategoryService.findCategoryById(productVo.getCategoryId()).getName()); // 获取商品分类名称

        List<LTImagesVo> ltImages = ltImagesService.getImagesList(productVo.getId(), LTImagesType.PRODUCT); // 获取商品关联的所有图片
        // 获取商品关联的所有图片
        productVo.setImages(ltImages);
        if (!ltImages.isEmpty()) {
            productVo.setImage(productVo.getImages().get(0));
        }

        return productVo;
    }

    @Override
    public LTWechatProductVo getProductRelationsById(Integer id, Long userId) {
        LTWechatProductVo productVo = this.convert(this.getById(id));
        LTUserVo userVo = userService.findUserById(productVo.getPublisherId()); // 获取发布者信息
        productVo.setAvatar(userVo.getAvatar());
        productVo.setNickName(userVo.getNickName());
        productVo.setCategoryName(ltCategoryService.findCategoryById(productVo.getCategoryId()).getName()); // 获取商品分类名称

        // 获取商品关联的所有图片
        productVo.setImages(ltImagesService.getImagesList(productVo.getId(), LTImagesType.PRODUCT));
        // 获取商品收藏、留言数量
        productVo.setCollectCount(ltCollectionService.findCollectionCount(productVo.getId(), LTCollectionTargetType.PRODUCT));
        productVo.setCommentCount(ltCommentService.findCommentCount(productVo.getId(), LTCommentType.LEAVE_MESSAGE));

        // 获取当前用户对商品收藏状态
        if (userId != null && userId > 0) {
            boolean isCollected = ltCollectionService.isCollection(productVo.getId(), LTCollectionTargetType.PRODUCT, userId); // 获取当前用户对商品收藏状态
            boolean isFollowed = ltUserFollowService.isFollow(userId, productVo.getPublisherId()); // 获取当前用户对发布者关注状态
            productVo.setIsFavorite(isCollected ? BooleanStatus.TRUE : BooleanStatus.FALSE);
            productVo.setIsFollowed(isFollowed ? BooleanStatus.TRUE : BooleanStatus.FALSE);
        }
        return productVo;
    }

    @Override
    @Cacheable(value = CacheKeyConstants.PRODUCT, key = "#id", unless = "#result == null")
    public LTWechatProductVo findProductById(Integer id) {
        return this.convert(this.getById(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(LTWechatProductVo productVo) {
        if (this.save(this.convert(productVo))) {
            this.cacheProductById(productVo.getId());
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(LTWeChatProductDTO productDTO) {
        if(this.save(this.convert(productDTO))) {
            this.cacheProductById(productDTO.getId());
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheKeyConstants.PRODUCT, key = "#productVo.id")
    public boolean update(LTWechatProductVo productVo) {
        return this.update(this.convert(productVo));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheKeyConstants.PRODUCT, key = "#productDTO.id")
    public boolean update(LTWeChatProductDTO productDTO) {
        LTProduct ltProduct = this.convert(productDTO);
        ltProduct.setUpdateTime(LocalDateTime.now());
        List<LTImagesVo> images = productDTO.getNewImages();
        if (images != null && !images.isEmpty()) {
            // 更新图片和商品关联
            for (LTImagesVo image : images) {
                image.setRelatedId(ltProduct.getId());
            }
            ltImagesService.batchUpdate(images);
        }
        return this.updateById(ltProduct);
    }

    @Override
    @CacheEvict(value = CacheKeyConstants.PRODUCT, key = "#ltProduct.id")
    @Transactional(rollbackFor = Exception.class)
    public boolean update(LTProduct ltProduct) {
        return this.updateById(ltProduct);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheKeyConstants.PRODUCT, key = "#id")
    public boolean deleteById(Integer id) {
        if (this.removeById(id)) {
            // 从数据库中查询商品关联的所有图片
            List<LTImagesVo> ltImages = ltImagesService.getImagesList(id, LTImagesType.PRODUCT);
            ltImagesService.batchDelete(ltImages);
            // 删除商品关联的留言、留言点赞、聊天消息
            ltCommentService.deleteByReferenceIdAndType(id, LTCommentType.LEAVE_MESSAGE);
            chatRelationService.deleteByProductId(id);
            ltLikeService.deleteByTargetId(id, LTLikeTargetType.LEAVE_MESSAGE);

            log.info("删除商品成功，商品编号：{}", id);
            return true;
        }
        return false;
    }

    @CachePut(value = CacheKeyConstants.PRODUCT, key = "#result.id", unless = "#result == null")
    public LTWechatProductVo cacheProductById(Integer id) {
        return this.convert(this.getById(id));
    }

    private LTProduct convert(LTWechatProductVo productVo) {
        LTProduct ltProduct = new LTProduct();
        BeanUtils.copyProperties(productVo, ltProduct);
        return ltProduct;
    }

    private LTWechatProductVo convert(LTProduct ltProduct) {
        LTWechatProductVo productVo = new LTWechatProductVo();
        BeanUtils.copyProperties(ltProduct, productVo);
        return productVo;
    }

    private LTProduct convert(LTWeChatProductDTO productDTO) {
        LTProduct ltProduct = new LTProduct();
        BeanUtils.copyProperties(productDTO, ltProduct);
        return ltProduct;
    }

    private int handleProductOperation(LTWeChatProductDTO productDTO, LTProductStatus status) {
        String desc = status.equals(LTProductStatus.STATUS_ON_SHELF) ? "发布" : "存草稿";
        try {
            productDTO.setStatus(status);
            productDTO.setPublishTime(LocalDateTime.now());

            LTProduct ltProduct = this.convert(productDTO);
            boolean insert = this.save(ltProduct);
            log.info("用户编号：{} 商品{}成功，商品编号：{}",
                    productDTO.getPublisherId(), desc, ltProduct.getId());
            List<LTImagesVo> images = productDTO.getNewImages();
            if (insert && images != null && !images.isEmpty()) {
                // 更新图片和商品关联
                for (LTImagesVo image : images) {
                    image.setRelatedId(ltProduct.getId());
                }
                ltImagesService.batchUpdate(images);
            }
            return insert ? ltProduct.getId() : 0;
        } catch (IllegalArgumentException e) {
            log.error(String.format("商品%s失败，参数校验不通过", desc), e);
            throw new LTException(String.format("商品%s失败，参数校验不通过", desc), e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    protected boolean handleProductStatus(LTWechatProductVo productVo, LTProductStatus status) {
        if (!ObjectUtils.isEmpty(productVo)) {
            productVo.setStatus(status); // 设置商品状态
            boolean update = this.update(productVo);
            log.info("商品编号：{} 更新商品状态为：{}", productVo.getStatus(), productVo.getId());
            Objects.requireNonNull(cacheManager.getCache(CacheKeyConstants.PRODUCT)).evict(productVo.getId());;
            return update;
        }
        return false;
    }

}
