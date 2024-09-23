package com.gxcy.letaotao.web.app.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.gxcy.letaotao.common.entity.LTPost;
import com.gxcy.letaotao.web.app.vo.LTWechatPostQueryVo;
import com.gxcy.letaotao.web.app.vo.LTWechatPostVo;
import org.apache.ibatis.annotations.Param;

/**
 * 帖子表 Mapper 接口
 */
public interface LTPostMapper extends BaseMapper<LTPost> {

    IPage<LTWechatPostVo> findListByPage(IPage<LTWechatPostVo> page,
                                         @Param(Constants.WRAPPER) QueryWrapper<LTWechatPostQueryVo> queryWrapper); // 分页查询帖子

}
