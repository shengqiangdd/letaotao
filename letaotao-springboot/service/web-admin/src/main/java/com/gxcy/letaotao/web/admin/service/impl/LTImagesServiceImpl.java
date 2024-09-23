package com.gxcy.letaotao.web.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxcy.letaotao.common.config.redis.CacheKeyConstants;
import com.gxcy.letaotao.common.entity.LTImages;
import com.gxcy.letaotao.common.enums.LTImagesType;
import com.gxcy.letaotao.web.admin.dto.LTImagesDTO;
import com.gxcy.letaotao.web.admin.mapper.LTImagesMapper;
import com.gxcy.letaotao.web.admin.service.FileService;
import com.gxcy.letaotao.web.admin.service.LTImagesService;
import com.gxcy.letaotao.web.admin.vo.LTImagesVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 图片表 服务实现类
 */
@Service
@Slf4j
public class LTImagesServiceImpl extends ServiceImpl<LTImagesMapper, LTImages> implements LTImagesService {

    @Resource
    private CacheManager cacheManager;
    @Resource
    private FileService fileService;

    @Override
    @Cacheable(value = CacheKeyConstants.IMAGES, keyGenerator = "customKeyGenerator", unless = "#result == null")
    public List<LTImagesVo> getImagesList(Integer relatedId, LTImagesType type) {
        // 通过关联id和类型查询图片
        return baseMapper.getImagesList(relatedId, type);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdate(List<LTImagesVo> ltImagesVos) {
        if (ltImagesVos != null && !ltImagesVos.isEmpty()) {
            boolean updated = this.updateBatchById(convert(ltImagesVos));
            if (updated) {
                ltImagesVos.forEach(ltImagesVo ->
                        Objects.requireNonNull(cacheManager.getCache(CacheKeyConstants.IMAGES)).evict(ltImagesVo.getId()));
            }
            return updated;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheKeyConstants.IMAGES, key = "#id")
    public boolean deleteById(Integer id) {
        LTImages images = this.getById(id);
        if (images != null) {
            // 删除文件
            fileService.delete(images.getUrl());
            log.info("图片删除成功，文件路径：：{}", images.getUrl());
            // 删除图片
            return this.removeById(id);
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CachePut(value = CacheKeyConstants.IMAGES, key = "#result.id", unless = "#result == null")
    public LTImagesVo upload(LTImagesDTO ltImagesDTO) {
        // 返回上传到oss的路径
        String url = fileService.upload(ltImagesDTO.getFile(),
                ltImagesDTO.getModule());
        LTImages ltImages = new LTImages();
        if (ObjectUtils.isEmpty(ltImagesDTO.getType())) {
            ltImages.setType(LTImagesType.USER); // 默认为用户模块
        } else {
            ltImages.setType(ltImagesDTO.getType()); // 指定模块
        }
        if (ObjectUtils.isEmpty(ltImages.getRelatedId())) {
            ltImages.setRelatedId(ltImagesDTO.getRelatedId()); // 指定关联id
        }
        ltImages.setUrl(url); // 文件路径
        // 保存图片信息
        if (this.save(ltImages)) {
            log.info("上传文件成功，文件路径：{}", url);
            return this.convert(ltImages);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheKeyConstants.IMAGES, key = "#ltImagesVo.id")
    public boolean update(LTImagesVo ltImagesVo) {
        return this.updateById(this.convert(ltImagesVo));
    }

    private List<LTImages> convert(List<LTImagesVo> ltImagesVos) {
        return ltImagesVos.stream().map(this::convert).collect(Collectors.toCollection(ArrayList::new));
    }

    private LTImages convert(LTImagesVo imagesVo) {
        LTImages images = new LTImages();
        BeanUtils.copyProperties(imagesVo, images);
        return images;
    }

    private LTImagesVo convert(LTImages images) {
        LTImagesVo imagesVo = new LTImagesVo();
        BeanUtils.copyProperties(images, imagesVo);
        return imagesVo;
    }
}
