package com.gxcy.letaotao.web.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gxcy.letaotao.web.admin.entity.Role;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role> {
    @Delete("delete from sys_role_permission where role_id = #{roleId}")
    void deleteRolePermission(Long roleId);

    /**
     * 保存角色权限关系
     *
     * @param roleId
     * @param permissionIds
     * @return
     */
    int saveRolePermission(Long roleId, List<Long> permissionIds);

    /**
     * 查询角色是否有用户绑定
     *
     * @param roleId
     * @return
     */
    @Select("select count(user_id) from sys_user_role where role_id=#{roleId}")
    int hasUserOfRole(Long roleId);

    /**
     * 根据用户ID查询该用户拥有的角色ID
     *
     * @param userId
     * @return
     */
    @Select("select role_id from sys_user_role where user_id = #{userId}")
    List<Long> findRoleIdByUserId(Long userId);
}
