package com.gxcy.letaotao.web.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxcy.letaotao.common.config.redis.CacheKeyConstants;
import com.gxcy.letaotao.common.entity.LTCategory;
import com.gxcy.letaotao.web.admin.mapper.LTCategoryMapper;
import com.gxcy.letaotao.web.admin.service.LTCategoryService;
import com.gxcy.letaotao.web.admin.vo.LTCategoryQueryVo;
import com.gxcy.letaotao.web.admin.vo.LTCategoryVo;
import com.gxcy.letaotao.web.admin.vo.SelectTree;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品分类表 服务实现类
 */
@Service
public class LTCategoryServiceImpl extends ServiceImpl<LTCategoryMapper, LTCategory> implements LTCategoryService {

    @Override
    @Cacheable(value = CacheKeyConstants.CATEGORY, keyGenerator = "customKeyGenerator", unless = "#result == null")
    public IPage<LTCategoryVo> findListByPage(IPage<LTCategoryVo> page, LTCategoryQueryVo categoryQueryVo) {
        // 创建条件构造器对象
        LambdaQueryWrapper<LTCategory> queryWrapper = new LambdaQueryWrapper<>();
        // 分类名
        queryWrapper.likeRight(!ObjectUtils.isEmpty(categoryQueryVo.getName()),
                LTCategory::getName, categoryQueryVo.getName());
        // 查询并返回数据
        return baseMapper.findListPage(page, queryWrapper);
    }

    @Override
    @Cacheable(value = CacheKeyConstants.CATEGORY, keyGenerator = "customKeyGenerator", unless = "#result == null")
    public List<LTCategoryVo> findCategoryList(LTCategoryQueryVo categoryQueryVo) {
        // 创建条件构造器对象
        LambdaQueryWrapper<LTCategory> queryWrapper = new LambdaQueryWrapper<>();

        // 分类编号
        queryWrapper.eq(!com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isEmpty(categoryQueryVo.getParentId()), LTCategory::getParentId, categoryQueryVo.getParentId());

        // 分类名称
        queryWrapper.eq(!com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isEmpty(categoryQueryVo.getName()), LTCategory::getName, categoryQueryVo.getName());

        return baseMapper.findList(queryWrapper);
    }

    @Override
    @Cacheable(value = CacheKeyConstants.CATEGORY, keyGenerator = "customKeyGenerator", unless = "#result == null")
    public List<SelectTree> getCategoryTree(LTCategoryQueryVo categoryQueryVo) {
        // 查询所有分类
        List<LTCategory> categories = list();

        // 构建树形结构
        List<SelectTree> tree = categories.stream().filter(category -> category.getParentId() == 0).map(category -> {
            SelectTree treeNode = new SelectTree();
            treeNode.setId(category.getId());
            treeNode.setValue(category.getId());
            treeNode.setLabel(category.getName());
            return treeNode;
        }).collect(Collectors.toList());

        // 构建父子关系
        for (SelectTree treeNode : tree) {
            List<SelectTree> children = categories.stream().filter(category -> category.getParentId().equals(treeNode.getId()))
                    .map(category -> {
                        SelectTree childNode = new SelectTree();
                        childNode.setId(category.getId());
                        childNode.setValue(category.getId());
                        childNode.setLabel(category.getName());
                        if (categoryQueryVo != null && categoryQueryVo.getHasImage())
                            childNode.setImageUrl(category.getImageUrl());
                        return childNode;
                    }).collect(Collectors.toList());
            treeNode.setChildren(children);
        }

        // 返回树形结构
        return tree;
    }

    @Override
    @Cacheable(value = CacheKeyConstants.CATEGORY, key = "#id")
    public LTCategoryVo findCategoryById(Integer id) {
        LTCategory ltCategory = this.getById(id);
        return convert(ltCategory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(LTCategoryVo categoryVo) {
        LTCategory ltCategory = convert(categoryVo);
        boolean save = save(ltCategory);

        if(save) {
            this.cacheCategoryById(ltCategory.getId());
        }
        return save;
    }

    @Override
    @CacheEvict(value = CacheKeyConstants.CATEGORY, key = "#categoryVo.id")
    @Transactional(rollbackFor = Exception.class)
    public boolean update(LTCategoryVo categoryVo) {
        LTCategory ltCategory = this.convert(categoryVo);
        return updateById(ltCategory);
    }

    @Override
    @CacheEvict(value = CacheKeyConstants.CATEGORY, key = "#id")
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Integer id) {
        return removeById(id);
    }

    private LTCategory convert(LTCategoryVo categoryVo) {
        LTCategory ltCategory = new LTCategory();
        BeanUtils.copyProperties(categoryVo, ltCategory);
        return ltCategory;
    }

    private LTCategoryVo convert(LTCategory ltCategory) {
        LTCategoryVo ltCategoryVo = new LTCategoryVo();
        BeanUtils.copyProperties(ltCategory, ltCategoryVo);
        return ltCategoryVo;
    }

    @CachePut(value = CacheKeyConstants.CATEGORY, key = "#result.id", unless = "#result == null")
    public LTCategoryVo cacheCategoryById(Integer id) {
        return this.convert(getById(id));
    }

}
