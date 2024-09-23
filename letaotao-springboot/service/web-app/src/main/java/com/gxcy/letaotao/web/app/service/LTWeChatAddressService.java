package com.gxcy.letaotao.web.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gxcy.letaotao.common.entity.LTAddress;
import com.gxcy.letaotao.web.app.vo.LTWechatAddressVo;

import java.util.List;

/**
 * 收货地址表 服务类
 */
public interface LTWeChatAddressService extends IService<LTAddress> {

    LTWechatAddressVo findDefaultAddress(Long userId);

    LTWechatAddressVo findAddressById(Integer id);

    List<LTWechatAddressVo> findAddressList(Long userId);

    boolean add(LTWechatAddressVo addressVo);

    boolean update(LTWechatAddressVo addressVo);

    boolean deleteById(Integer id);

}
