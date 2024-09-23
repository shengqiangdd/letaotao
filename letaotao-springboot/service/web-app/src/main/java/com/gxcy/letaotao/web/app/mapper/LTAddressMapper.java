package com.gxcy.letaotao.web.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gxcy.letaotao.common.entity.LTAddress;
import com.gxcy.letaotao.web.app.vo.LTWechatAddressVo;
import org.apache.ibatis.annotations.Select;

/**
 * 收货地址表 Mapper 接口
 */
public interface LTAddressMapper extends BaseMapper<LTAddress> {

    @Select("select `id`,`contact_person`,`phone`,`region`,`detail_address` " +
            "from lt_address where user_id = #{userId} and is_default = 1")
    LTWechatAddressVo findDefaultAddress(Long userId); // 查询默认收货地址

}
