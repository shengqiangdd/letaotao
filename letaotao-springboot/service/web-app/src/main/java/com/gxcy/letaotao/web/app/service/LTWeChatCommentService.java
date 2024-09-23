package com.gxcy.letaotao.web.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gxcy.letaotao.common.entity.LTComment;
import com.gxcy.letaotao.common.enums.LTCommentType;
import com.gxcy.letaotao.web.app.vo.LTWechatCommentQueryVo;
import com.gxcy.letaotao.web.app.vo.LTWechatCommentVo;

import java.util.List;

/**
 * 留言/评论表 服务类
 */
public interface LTWeChatCommentService extends IService<LTComment> {

    List<LTWechatCommentVo> findList(LTWechatCommentQueryVo commentVO);

    List<LTWechatCommentVo> getCommentTree(LTWechatCommentQueryVo commentVO);

    int findCommentCount(Integer referenceId, LTCommentType type);

    void deleteByReferenceIdAndType(Integer referenceId, LTCommentType type);

    LTWechatCommentVo findCommentById(Integer id);

    LTWechatCommentVo add(LTWechatCommentVo commentVo);

    boolean update(LTWechatCommentVo commentVo);

    boolean deleteById(Integer id);
}
