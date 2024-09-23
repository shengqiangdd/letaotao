package com.gxcy.letaotao.web.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gxcy.letaotao.common.entity.LTCategory;
import com.gxcy.letaotao.web.admin.vo.LTCategoryQueryVo;
import com.gxcy.letaotao.web.admin.vo.LTCategoryVo;
import com.gxcy.letaotao.web.admin.vo.SelectTree;

import java.util.List;

/**
 * 商品分类表 服务类
 */
public interface LTCategoryService extends IService<LTCategory> {

    IPage<LTCategoryVo> findListByPage(IPage<LTCategoryVo> page, LTCategoryQueryVo categoryQueryVo);

    List<LTCategoryVo> findCategoryList(LTCategoryQueryVo categoryQueryVo);

    List<SelectTree> getCategoryTree(LTCategoryQueryVo categoryQueryVo);

    LTCategoryVo findCategoryById(Integer id);

    boolean add(LTCategoryVo categoryVo);

    boolean update(LTCategoryVo categoryVo);

    boolean deleteById(Integer id);

}
