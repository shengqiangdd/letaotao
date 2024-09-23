package com.gxcy.letaotao.web.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gxcy.letaotao.common.entity.LTPost;
import com.gxcy.letaotao.web.app.dto.LTWeChatPostDTO;
import com.gxcy.letaotao.web.app.vo.LTWechatPostQueryVo;
import com.gxcy.letaotao.web.app.vo.LTWechatPostVo;

public interface LTWeChatPostService extends BaseService<LTPost> {

    IPage<LTWechatPostVo> findListByPage(IPage<LTWechatPostVo> page, LTWechatPostQueryVo postQueryVo);

    IPage<LTWechatPostVo> findListByUserId(IPage<LTWechatPostVo> page, LTWechatPostQueryVo postQueryVo);

    int publish(LTWeChatPostDTO postDTO);

    int save(LTWeChatPostDTO postDTO);

    LTWechatPostVo findPostById(Integer id);

    boolean add(LTWechatPostVo postVo);

    boolean add(LTWeChatPostDTO postDTO);

    boolean update(LTWechatPostVo postVo);

    boolean update(LTWeChatPostDTO postDTO);

    boolean deleteById(Integer id);

    LTWechatPostVo findRelation(Long id, Long userId);
}
