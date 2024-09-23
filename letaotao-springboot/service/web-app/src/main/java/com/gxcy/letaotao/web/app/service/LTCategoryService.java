package com.gxcy.letaotao.web.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gxcy.letaotao.common.entity.LTCategory;
import com.gxcy.letaotao.web.app.vo.LTCategoryQueryVo;
import com.gxcy.letaotao.web.app.vo.LTCategoryVo;
import com.gxcy.letaotao.web.app.vo.SelectTree;

import java.util.List;

/**
 * 商品分类表 服务类
 */
public interface LTCategoryService extends IService<LTCategory> {

    List<SelectTree> getCategoryTree(LTCategoryQueryVo categoryQueryVo);

    LTCategoryVo findCategoryById(Integer id);

}
