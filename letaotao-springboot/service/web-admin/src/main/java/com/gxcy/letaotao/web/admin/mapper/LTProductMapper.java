package com.gxcy.letaotao.web.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.gxcy.letaotao.common.entity.LTProduct;
import com.gxcy.letaotao.web.admin.vo.LTProductQueryVo;
import com.gxcy.letaotao.web.admin.vo.LTProductVo;
import org.apache.ibatis.annotations.Param;

/**
 * 商品表 Mapper 接口
 */
public interface LTProductMapper extends BaseMapper<LTProduct> {

    IPage<LTProductVo> findListByPage(IPage<LTProductVo> page,
                                      @Param(Constants.WRAPPER) QueryWrapper<LTProductQueryVo> queryWrapper); // 分页查询商品

}
