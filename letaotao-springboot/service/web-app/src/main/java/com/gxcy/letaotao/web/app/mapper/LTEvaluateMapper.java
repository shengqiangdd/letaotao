package com.gxcy.letaotao.web.app.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.gxcy.letaotao.web.app.entity.LTEvaluate;
import com.gxcy.letaotao.web.app.vo.LTWechatEvaluateVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 评价表 Mapper 接口
 */
public interface LTEvaluateMapper extends BaseMapper<LTEvaluate> {

    @Select("select exists (select 1 from lt_evaluate where order_id = #{orderId} and user_id = #{userId})")
    boolean isEvaluate(Integer orderId, Long userId); // 判断用户是否评价过该订单

    @Select("select e.`id`,`order_id`,e.`user_id`,`is_favor`,`content`,e.`create_time`,u.`nick_name`,u.`avatar` " +
            "from lt_evaluate e left join lt_user u on u.`id` = e.`user_id` ${ew.customSqlSegment}")
    List<LTWechatEvaluateVo> listByWrapper(@Param(Constants.WRAPPER) LambdaQueryWrapper<LTEvaluate> queryWrapper);

}
