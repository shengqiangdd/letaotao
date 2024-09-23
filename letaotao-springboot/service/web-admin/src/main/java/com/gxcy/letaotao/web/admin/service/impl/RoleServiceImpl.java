package com.gxcy.letaotao.web.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxcy.letaotao.common.config.redis.CacheKeyConstants;
import com.gxcy.letaotao.common.enums.BooleanStatus;
import com.gxcy.letaotao.common.exception.BizException;
import com.gxcy.letaotao.web.admin.entity.Role;
import com.gxcy.letaotao.web.admin.entity.User;
import com.gxcy.letaotao.web.admin.mapper.RoleMapper;
import com.gxcy.letaotao.web.admin.mapper.UserMapper;
import com.gxcy.letaotao.web.admin.service.RoleService;
import com.gxcy.letaotao.web.admin.vo.RoleQueryVo;
import jakarta.annotation.Resource;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Objects;


@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private CacheManager cacheManager;

    /**
     * 根据用户查询角色列表
     *
     * @param page
     * @param roleQueryVo
     * @return
     */
    @Override
    @Cacheable(value = CacheKeyConstants.ROLE, keyGenerator = "customKeyGenerator", unless = "#result == null")
    public IPage<Role> findRoleListByUserId(IPage<Role> page, RoleQueryVo roleQueryVo) {
        // 创建条件构造器
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        // 角色名称
        queryWrapper.like(!ObjectUtils.isEmpty(roleQueryVo.getRoleName()), Role::getRoleName, roleQueryVo.getRoleName());
        // 排序
        queryWrapper.orderByAsc(Role::getId);
        // 根据用户ID查询用户信息
        User user = userMapper.selectById(roleQueryVo.getUserId());
        // 如果用户不为空，且不是管理员，则只能查询自己创建的角色
        if (user != null && !ObjectUtils.isEmpty(user.getIsAdmin()) && user.getIsAdmin() != BooleanStatus.TRUE) {
            queryWrapper.eq(Role::getCreateUser, roleQueryVo.getUserId());
        }
        return baseMapper.selectPage(page, queryWrapper);
    }

    /**
     * 保存角色权限关系
     *
     * @param roleId
     * @param permissionIds
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRolePermission(Long roleId, List<Long> permissionIds) {
        // 删除该角色对应的权限信息
        deleteRolePermission(roleId);
        // 保存角色权限
        if(baseMapper.saveRolePermission(roleId, permissionIds) > 0) {
            for (Long permissionId : permissionIds) {
                Objects.requireNonNull(cacheManager.getCache(CacheKeyConstants.PERMISSION)).evict(permissionId);
            }
            Objects.requireNonNull(cacheManager.getCache(CacheKeyConstants.ROLE)).evict(roleId);
            return true;
        }
        return false;
    }

    /**
     * 查询角色是否有用户绑定
     *
     * @param roleId
     * @return
     */
    @Override
    public boolean hasUserOfRole(Long roleId) {
        // 查询角色是否有用户绑定
        return baseMapper.hasUserOfRole(roleId) > 0;
    }

    /**
     * 删除角色
     *
     * @param roleId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRolePermission(Long roleId) {
        // 删除该角色对应的权限信息
        baseMapper.deleteRolePermission(roleId);
        Objects.requireNonNull(cacheManager.getCache(CacheKeyConstants.ROLE)).evict(roleId);
    }

    /**
     * 根据用户ID查询该用户拥有的角色ID
     *
     * @param userId
     * @return
     */
    @Override
    public List<Long> findRoleIdByUserId(Long userId) {
        // 根据用户ID查询该用户拥有的角色ID
        return baseMapper.findRoleIdByUserId(userId);
    }

    @Override
    @Cacheable(value = CacheKeyConstants.ROLE, key = "#id", unless = "#result == null")
    public Role findRoleById(Long id) {
        return this.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(Role role) {
        if(this.save(role)) {
            this.cacheRoleById(role.getId());
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheKeyConstants.ROLE, key = "#role.id", condition = "#result != null")
    public boolean update(Role role) {
        return this.updateById(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheKeyConstants.ROLE, key = "#id")
    public boolean deleteById(Long id) {
        if (this.hasUserOfRole(id)) {
            throw new BizException("该角色已被其他用户绑定，不可删除");
        } else {
            // 删除角色
            if (this.removeById(id)) {
                // 删除角色相关权限
                this.deleteRolePermission(id);
                return true;
            }
        }
        return false;
    }

    @CachePut(value = CacheKeyConstants.ROLE, key = "#result.id", condition = "#result != null")
    public Role cacheRoleById(Long id) {
        return this.getById(id);
    }
}
