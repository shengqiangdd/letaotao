package com.gxcy.letaotao.web.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxcy.letaotao.common.config.redis.CacheKeyConstants;
import com.gxcy.letaotao.common.entity.LTProduct;
import com.gxcy.letaotao.common.enums.LTImagesType;
import com.gxcy.letaotao.web.admin.mapper.LTCategoryMapper;
import com.gxcy.letaotao.web.admin.mapper.LTImagesMapper;
import com.gxcy.letaotao.web.admin.mapper.LTProductMapper;
import com.gxcy.letaotao.web.admin.service.LTProductService;
import com.gxcy.letaotao.web.admin.vo.LTImagesVo;
import com.gxcy.letaotao.web.admin.vo.LTProductQueryVo;
import com.gxcy.letaotao.web.admin.vo.LTProductVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 商品表 服务实现类
 */
@Service
@Slf4j
public class LTProductServiceImpl extends ServiceImpl<LTProductMapper, LTProduct> implements LTProductService {

    @Resource
    private LTCategoryMapper categoryMapper;
    @Resource
    private LTImagesMapper ltImagesMapper;
    @Resource
    private CacheManager cacheManager;

    @Override
    @Cacheable(value = CacheKeyConstants.PRODUCT, keyGenerator = "customKeyGenerator", unless = "#result == null")
    public IPage<LTProductVo> findListByPage(IPage<LTProductVo> page, LTProductQueryVo productQueryVo) {
        // 创建条件构造器对象
        QueryWrapper<LTProductQueryVo> queryWrapper = new QueryWrapper<>();

        // 用户昵称
        queryWrapper.likeRight(!ObjectUtils.isEmpty(productQueryVo.getNickName()),
                "u.nick_name", productQueryVo.getNickName());
        // 商品名
        queryWrapper.likeRight(!ObjectUtils.isEmpty(productQueryVo.getName()),
                "p.`name`", productQueryVo.getName());
        // 商品描述
        queryWrapper.likeRight(!ObjectUtils.isEmpty(productQueryVo.getDescription()),
                "p.description", productQueryVo.getDescription());
        // 商品分类
        queryWrapper.eq(!ObjectUtils.isEmpty(productQueryVo.getCategoryId())
                        && productQueryVo.getCategoryId() > 0,
                "p.category_id", productQueryVo.getCategoryId());

        // 发布时间
        if (!ObjectUtils.isEmpty(productQueryVo.getBetweenTime())) {
            String[] betweenTime = productQueryVo.getBetweenTime();
            Timestamp startTime = Timestamp.valueOf(betweenTime[0]);
            Timestamp endTime = Timestamp.valueOf(betweenTime[1]);
            queryWrapper.between("p.publish_time", startTime, endTime);
        }

        // 商品状态
        queryWrapper.eq(!ObjectUtils.isEmpty(productQueryVo.getStatus()),
                "p.status", productQueryVo.getStatus());
        // 是否推荐
        queryWrapper.eq(!ObjectUtils.isEmpty(productQueryVo.getIsRecommended()),
                "p.is_recommended", productQueryVo.getIsRecommended());

        IPage<LTProductVo> list = baseMapper.findListByPage(page, queryWrapper);
        List<LTProductVo> ltProducts = list.getRecords();
        for (LTProductVo ltProduct : ltProducts) {
            List<LTImagesVo> images = ltImagesMapper.getImagesList(ltProduct.getId(), LTImagesType.PRODUCT); // 商品图片
            ltProduct.setImages(images);
        }

        // 查询并返回数据
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Cacheable(value = CacheKeyConstants.PRODUCT, key = "#result.id", unless = "#result == null")
    public LTProductVo findProductById(Integer id) {
        return this.convert(this.getById(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(LTProductVo productVo) {
        if (this.save(this.convert(productVo))) {
            this.cacheProductById(productVo.getId());
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheKeyConstants.PRODUCT, key = "#productVo.id")
    public boolean update(LTProductVo productVo) {
        return this.updateById(this.convert(productVo));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdate(List<LTProductVo> ltProductVos) {
        if (this.updateBatchById(this.convert(ltProductVos))) {
            ltProductVos.forEach(ltProduct -> {
                Objects.requireNonNull(cacheManager.getCache(CacheKeyConstants.PRODUCT)).evictIfPresent(ltProduct.getId());
            });
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheKeyConstants.PRODUCT, key = "#id")
    public boolean deleteById(Integer id) {
        return this.removeById(id);
    }

    @CachePut(value = CacheKeyConstants.PRODUCT, key = "#result.id", unless = "#result == null")
    public LTProductVo cacheProductById(Integer id) {
        return this.convert(this.getById(id));
    }

    private LTProduct convert(LTProductVo productVo) {
        LTProduct ltProduct = new LTProduct();
        BeanUtils.copyProperties(productVo, ltProduct);
        return ltProduct;
    }

    private LTProductVo convert(LTProduct ltProduct) {
        LTProductVo productVo = new LTProductVo();
        BeanUtils.copyProperties(ltProduct, productVo);
        return productVo;
    }

    private List<LTProduct> convert(List<LTProductVo> productVos) {
        return productVos.stream().map(this::convert).collect(Collectors.toList());
    }

}
