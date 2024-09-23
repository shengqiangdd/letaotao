package com.gxcy.letaotao.web.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gxcy.letaotao.common.entity.LTPost;
import com.gxcy.letaotao.web.admin.vo.LTPostQueryVo;
import com.gxcy.letaotao.web.admin.vo.LTPostVo;

import java.util.List;

/**
 * 帖子表 服务类
 */
public interface LTPostService extends IService<LTPost> {

    IPage<LTPostVo> findListByPage(IPage<LTPostVo> page, LTPostQueryVo postQueryVo);

    LTPostVo findPostById(Integer id);

    boolean add(LTPostVo postVo);

    boolean update(LTPostVo postVo);

    boolean batchUpdate(List<LTPostVo> ltPostVos);

    boolean deleteById(Integer id);
}
