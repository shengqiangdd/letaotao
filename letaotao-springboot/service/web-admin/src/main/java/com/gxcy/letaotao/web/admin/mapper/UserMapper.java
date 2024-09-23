package com.gxcy.letaotao.web.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.gxcy.letaotao.web.admin.entity.User;
import com.gxcy.letaotao.web.admin.vo.UserVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    /**
     * 删除用户角色关系
     *
     * @param userId
     * @return
     */
    @Delete("delete from sys_user_role where user_id = #{userId}")
    int deleteUserRole(Long userId);

    /**
     * 保存用户角色关系
     *
     * @param userId
     * @param roleIds
     * @return
     */
    int saveUserRole(Long userId, List<Long> roleIds);

    IPage<UserVo> findListByPage(IPage<UserVo> page,
                                 @Param(Constants.WRAPPER) LambdaQueryWrapper<User> queryWrapper); // 分页查询用户

    @Select("select `id`,username,password,is_account_non_expired,is_account_non_locked," +
            "is_credentials_non_expired,is_enabled,real_name,nick_name,gender,phone,email," +
            "avatar,introduction,birthday,is_admin,create_time,update_time,is_delete " +
            "from sys_user where username = #{username}")
    UserVo findUserByUserName(String username);
}
