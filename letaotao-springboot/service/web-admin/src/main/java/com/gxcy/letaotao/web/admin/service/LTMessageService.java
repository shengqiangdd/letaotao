package com.gxcy.letaotao.web.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gxcy.letaotao.common.entity.LTMessage;
import com.gxcy.letaotao.web.admin.vo.LTMessageQueryVo;
import com.gxcy.letaotao.web.admin.vo.LTMessageVo;

/**
 * 通知/消息表 服务类
 */
public interface LTMessageService extends IService<LTMessage> {

    IPage<LTMessageVo> findListByPage(IPage<LTMessageVo> page, LTMessageQueryVo messageQueryVo);

    LTMessageVo findMessageById(Integer id);

    boolean add(LTMessageVo messageVo);

    boolean update(LTMessageVo messageVo);

    boolean deleteById(Integer id);
}
