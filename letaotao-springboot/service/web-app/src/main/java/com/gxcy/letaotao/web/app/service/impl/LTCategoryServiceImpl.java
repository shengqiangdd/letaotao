package com.gxcy.letaotao.web.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxcy.letaotao.common.config.redis.CacheKeyConstants;
import com.gxcy.letaotao.common.entity.LTCategory;
import com.gxcy.letaotao.web.app.mapper.LTCategoryMapper;
import com.gxcy.letaotao.web.app.service.LTCategoryService;
import com.gxcy.letaotao.web.app.vo.LTCategoryQueryVo;
import com.gxcy.letaotao.web.app.vo.LTCategoryVo;
import com.gxcy.letaotao.web.app.vo.SelectTree;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品分类表 服务实现类
 */
@Service
public class LTCategoryServiceImpl extends ServiceImpl<LTCategoryMapper, LTCategory> implements LTCategoryService {

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
    @Cacheable(value = CacheKeyConstants.CATEGORY, key = "#id", unless = "#result == null")
    public LTCategoryVo findCategoryById(Integer id) {
        return this.convert(this.getById(id));
    }

    private LTCategoryVo convert(LTCategory category) {
        LTCategoryVo categoryVo = new LTCategoryVo();
        BeanUtils.copyProperties(category, categoryVo);
        return categoryVo;
    }

}
