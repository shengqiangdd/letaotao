package com.gxcy.letaotao.web.app.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.gxcy.letaotao.web.app.entity.LTUser;
import com.gxcy.letaotao.web.app.vo.LTUserVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Author csq
 * @Date 2024/9/13 20:44
 * 用户表 Mapper 接口
 */
public interface LTUserMapper extends BaseMapper<LTUser> {

    @Select("select `id`,`username`,`real_name`,`nick_name`,`gender`,`phone`,`email`,`avatar`," +
            "`introduction`,`birthday`,`open_id`,`create_time`,`update_time`,`is_delete` " +
            "from lt_user where is_delete = 0 and `open_id` = #{openId}")
    LTUserVo findUserByOpenId(String openId);

    @Select("select `id`,`username`,`real_name`,`nick_name`,`gender`,`phone`,`email`,`avatar`," +
            "`introduction`,`birthday`,`open_id`,`create_time`,`update_time`,`is_delete` " +
            "from lt_user where is_delete = 0 and `id` = #{id}")
    LTUserVo findUserById(Long id);

    IPage<LTUserVo> findListByPage(IPage<LTUserVo> page,
                                   @Param(Constants.WRAPPER) LambdaQueryWrapper<LTUser> queryWrapper); // 分页查询用户

}
