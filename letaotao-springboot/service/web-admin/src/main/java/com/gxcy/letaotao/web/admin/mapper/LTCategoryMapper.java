package com.gxcy.letaotao.web.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.gxcy.letaotao.common.entity.LTCategory;
import com.gxcy.letaotao.web.admin.vo.LTCategoryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品分类表 Mapper 接口
 */
public interface LTCategoryMapper extends BaseMapper<LTCategory> {

    IPage<LTCategoryVo> findListPage(IPage<LTCategoryVo> page,
                                     @Param(Constants.WRAPPER) LambdaQueryWrapper<LTCategory> categoryQueryVo);

    List<LTCategoryVo> findList(@Param(Constants.WRAPPER) LambdaQueryWrapper<LTCategory> categoryQueryVo);

}
