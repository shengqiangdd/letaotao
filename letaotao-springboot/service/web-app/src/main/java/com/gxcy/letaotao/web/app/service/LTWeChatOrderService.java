package com.gxcy.letaotao.web.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gxcy.letaotao.common.entity.LTOrder;
import com.gxcy.letaotao.web.app.vo.LTWeChatOrderVo;
import com.gxcy.letaotao.web.app.vo.LTWechatOrderQueryVo;

public interface LTWeChatOrderService extends BaseService<LTOrder> {

    IPage<LTWeChatOrderVo> findListByPage(IPage<LTWeChatOrderVo> page, LTWechatOrderQueryVo ltOrderQueryVo);

    LTWeChatOrderVo getOrderByChatRelation(LTWeChatOrderVo ltWeChatOrderVO);

    LTWeChatOrderVo createOrder(LTWeChatOrderVo ltWeChatOrderVO);

    boolean isProductAvailableForOrder(Integer productId);

    LTWeChatOrderVo getOrderById(Integer id);

    boolean add(LTWeChatOrderVo orderVo);

    LTWeChatOrderVo update(LTWeChatOrderVo orderVo);

    boolean deleteById(Integer id);

    LTWeChatOrderVo getOrderRelation(Integer id, Long currentUserId);

    boolean pay(LTWeChatOrderVo ltWeChatOrderVO);

    LTWeChatOrderVo deliver(LTWeChatOrderVo orderVo);

    LTWeChatOrderVo receive(LTWeChatOrderVo orderVo);

    LTWeChatOrderVo cancel(LTWeChatOrderVo orderVo);
}
