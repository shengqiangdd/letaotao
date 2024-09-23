package com.gxcy.letaotao.web.app.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.gxcy.letaotao.common.entity.LTProduct;
import com.gxcy.letaotao.web.app.vo.LTWechatProductQueryVo;
import com.gxcy.letaotao.web.app.vo.LTWechatProductVo;
import org.apache.ibatis.annotations.Param;

/**
 * 商品表 Mapper 接口
 */
public interface LTProductMapper extends BaseMapper<LTProduct> {

    IPage<LTWechatProductVo> findListByPage(IPage<LTWechatProductVo> page,
                                            @Param(Constants.WRAPPER) QueryWrapper<LTWechatProductQueryVo> queryWrapper); // 分页查询商品

}
