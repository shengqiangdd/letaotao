package com.gxcy.letaotao.web.admin.service;

import com.gxcy.letaotao.web.admin.entity.Permission;
import com.gxcy.letaotao.web.admin.vo.PermissionQueryVo;
import com.gxcy.letaotao.web.admin.vo.PermissionVo;
import com.gxcy.letaotao.web.admin.vo.RolePermissionVo;

import java.util.List;


public interface PermissionService extends BaseService<Permission> {
    /**
     * 根虎用户名ID查询权限列表
     *
     * @param userId
     * @return
     */
    List<PermissionVo> findPermissionListByUserId(Long userId);

    /**
     * 查询菜单列表
     *
     * @param permissionQueryVo
     * @return
     */
    List<PermissionVo> findPermissionList(PermissionQueryVo permissionQueryVo);

    /**
     * 查询上级菜单列表
     *
     * @return
     */
    List<PermissionVo> findParentPermissionList();

    /**
     * 检查菜单是否有子菜单
     *
     * @param id
     * @return
     */
    boolean hasChildrenOfPermission(Long id);

    /**
     * 查询分配权限树列表
     *
     * @param userId
     * @param roleId
     * @return
     */
    RolePermissionVo findPermissionTree(Long userId, Long roleId);


    /**
     * 绑定菜单权限
     *
     * @param userId
     * @param permissionIds
     * @return
     */
    boolean saveUserPermission(Long userId, List<Long> permissionIds);

    PermissionVo findPermissionById(Long id);

    boolean add(PermissionVo permissionVo);

    boolean update(PermissionVo permissionVo);

    boolean deleteById(Long id);
}
