package com.gxcy.letaotao.web.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.gxcy.letaotao.web.admin.entity.Permission;
import com.gxcy.letaotao.web.admin.vo.PermissionVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission> {
    /**
     * 根虎用户名ID查询权限列表
     *
     * @param userId
     * @return
     */
    List<PermissionVo> findPermissionListByUserId(Long userId);

    /**
     * 根据角色ID查询权限列表
     *
     * @param roleId
     * @return
     */
    List<PermissionVo> findPermissionListByRoleId(Long roleId);

    List<PermissionVo> findPermissionList(@Param(Constants.WRAPPER) LambdaQueryWrapper<Permission> queryWrapper);
}
