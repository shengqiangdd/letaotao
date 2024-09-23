package com.gxcy.letaotao.web.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.gxcy.letaotao.common.entity.LTPost;
import com.gxcy.letaotao.web.admin.vo.LTPostQueryVo;
import com.gxcy.letaotao.web.admin.vo.LTPostVo;
import org.apache.ibatis.annotations.Param;

/**
 * 帖子表 Mapper 接口
 */
public interface LTPostMapper extends BaseMapper<LTPost> {

    IPage<LTPostVo> findListByPage(IPage<LTPostVo> page,
                                   @Param(Constants.WRAPPER) QueryWrapper<LTPostQueryVo> queryWrapper); // 分页查询帖子

}
