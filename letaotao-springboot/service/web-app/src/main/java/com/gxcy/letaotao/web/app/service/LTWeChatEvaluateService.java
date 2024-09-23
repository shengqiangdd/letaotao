package com.gxcy.letaotao.web.app.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gxcy.letaotao.web.app.entity.LTEvaluate;
import com.gxcy.letaotao.web.app.vo.LTWechatEvaluateVo;

import java.util.List;

/**
 * 评价表 服务类
 */
public interface LTWeChatEvaluateService extends IService<LTEvaluate> {

    List<LTWechatEvaluateVo> getEvaluateList(LambdaQueryWrapper<LTEvaluate> queryWrapper);

    List<LTWechatEvaluateVo> getEvaluateListByUserId(Long userId);

    List<LTWechatEvaluateVo> getEvaluateListByOrderId(Long orderId);

    boolean isEvaluate(Integer orderId, Long userId);

    LTWechatEvaluateVo add(LTWechatEvaluateVo evaluateVo);
}
