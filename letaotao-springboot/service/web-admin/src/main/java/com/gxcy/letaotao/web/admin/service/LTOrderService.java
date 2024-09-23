package com.gxcy.letaotao.web.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gxcy.letaotao.common.entity.LTOrder;
import com.gxcy.letaotao.web.admin.vo.LTOrderQueryVo;
import com.gxcy.letaotao.web.admin.vo.LTOrderVo;

/**
 * 订单表 服务类
 */
public interface LTOrderService extends IService<LTOrder> {

    IPage<LTOrderVo> findListByPage(IPage<LTOrderVo> page, LTOrderQueryVo orderQueryVo);

    LTOrderVo findOrderById(Integer id);

    boolean add(LTOrderVo orderVo);

    boolean update(LTOrderVo orderVo);

    boolean deleteById(Integer id);
}
