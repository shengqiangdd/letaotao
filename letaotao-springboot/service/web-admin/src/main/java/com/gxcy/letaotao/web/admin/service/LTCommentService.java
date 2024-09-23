package com.gxcy.letaotao.web.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gxcy.letaotao.common.entity.LTComment;
import com.gxcy.letaotao.web.admin.vo.LTCommentQueryVo;
import com.gxcy.letaotao.web.admin.vo.LTCommentVo;

/**
 * 留言/评论表 服务类
 */
public interface LTCommentService extends IService<LTComment> {

    IPage<LTCommentVo> findListByPage(IPage<LTCommentVo> page, LTCommentQueryVo commentQueryVo);

    LTCommentVo findCommentById(Integer id);

    boolean add(LTCommentVo commentVo);

    boolean update(LTCommentVo commentVo);

    boolean deleteById(Integer id);
}
