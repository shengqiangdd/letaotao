package com.gxcy.letaotao.web.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.gxcy.letaotao.common.entity.LTOrder;
import com.gxcy.letaotao.web.admin.vo.LTOrderQueryVo;
import com.gxcy.letaotao.web.admin.vo.LTOrderVo;
import org.apache.ibatis.annotations.Param;

/**
 * 订单表 Mapper 接口
 */
public interface LTOrderMapper extends BaseMapper<LTOrder> {

    IPage<LTOrderVo> findListByPage(IPage<LTOrderVo> page,
                                    @Param(Constants.WRAPPER) QueryWrapper<LTOrderQueryVo> queryWrapper); // 分页查询订单

}
