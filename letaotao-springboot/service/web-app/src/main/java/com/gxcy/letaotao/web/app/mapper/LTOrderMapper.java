package com.gxcy.letaotao.web.app.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.gxcy.letaotao.common.entity.LTOrder;
import com.gxcy.letaotao.web.app.vo.LTWeChatOrderVo;
import com.gxcy.letaotao.web.app.vo.LTWechatOrderQueryVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 订单表 Mapper 接口
 */
public interface LTOrderMapper extends BaseMapper<LTOrder> {

    IPage<LTWeChatOrderVo> findListByPage(IPage<LTWeChatOrderVo> page,
                                          @Param(Constants.WRAPPER) QueryWrapper<LTWechatOrderQueryVo> queryWrapper); // 分页查询订单

    @Select("select lock_by from lt_product where is_lock = 1 and `id` = #{productId}")
    String checkProductLockedStatus(Integer productId); // 检查商品是否被锁定

    @Select("select id,order_num,buyer_id,seller_id,product_id,price,address_id,status," +
            "create_time,pay_time,ship_time,finish_time from lt_order where is_delete = 0 and seller_id = #{sellerId} " +
            "and buyer_id = #{buyerId} and product_id = #{productId} and status <> 5")
    LTWeChatOrderVo getOrderByChatRelation(LTWeChatOrderVo ltWeChatOrderVO); // 通过聊天关系获取订单

    @Select("select id,order_num,buyer_id,seller_id,product_id,price,address_id,status," +
            "create_time,pay_time,ship_time,finish_time from lt_order where is_delete = 0 and id = #{id}")
    LTWeChatOrderVo getOrderById(Integer id); // 通过订单ID获取订单

}
