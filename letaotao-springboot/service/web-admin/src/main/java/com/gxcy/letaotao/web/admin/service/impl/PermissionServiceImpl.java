package com.gxcy.letaotao.web.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gxcy.letaotao.common.config.redis.CacheKeyConstants;
import com.gxcy.letaotao.common.enums.BooleanStatus;
import com.gxcy.letaotao.common.enums.PermissionType;
import com.gxcy.letaotao.web.admin.entity.Permission;
import com.gxcy.letaotao.web.admin.entity.User;
import com.gxcy.letaotao.web.admin.mapper.PermissionMapper;
import com.gxcy.letaotao.web.admin.mapper.RoleMapper;
import com.gxcy.letaotao.web.admin.mapper.UserMapper;
import com.gxcy.letaotao.web.admin.service.PermissionService;
import com.gxcy.letaotao.web.admin.vo.*;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;


@Service
public class PermissionServiceImpl extends BaseServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private CacheManager cacheManager;

    /**
     * 根虎用户名ID查询权限列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<PermissionVo> findPermissionListByUserId(Long userId) {
        //调用查询权限列表的方法
        return baseMapper.findPermissionListByUserId(userId);
    }

    /**
     * 查询菜单列表
     *
     * @param permissionQueryVo
     * @return
     */
    @Override
    @Cacheable(value = CacheKeyConstants.PERMISSION, keyGenerator = "customKeyGenerator", unless = "#result == null")
    public List<PermissionVo> findPermissionList(PermissionQueryVo permissionQueryVo) {
        // 创建条件构造器对象
        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
        // 排序
        queryWrapper.orderByAsc(Permission::getOrderNum);
        // 调用查询菜单列表的方法
        List<PermissionVo> permissionList = baseMapper.findPermissionList(queryWrapper);
        // 生成菜单树返回数据
        return MenuTree.makeMenuTree(permissionList, 0L);
    }

    /**
     * 查询上级菜单列表
     *
     * @return
     */
    @Override
    @Cacheable(value = CacheKeyConstants.PERMISSION, keyGenerator = "customKeyGenerator", unless = "#result == null")
    public List<PermissionVo> findParentPermissionList() {
        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
        // 只查询type为目录和菜单的数据(type=0或type=1)
        queryWrapper.in(Permission::getType, Arrays.asList(PermissionType.DIR, PermissionType.MENU));
        // 排序
        queryWrapper.orderByAsc(Permission::getOrderNum);
        // 查询菜单数据
        List<PermissionVo> permissionList = baseMapper.findPermissionList(queryWrapper);
        // 构造顶级菜单信息，如果数据库中的菜单表没有数据，选择上级菜单时则显示顶级菜单
        PermissionVo permission = new PermissionVo();
        permission.setId(0L);
        permission.setParentId(-1L);
        permission.setLabel("顶级菜单");
        permissionList.add(permission);//将顶级菜单添加到集合
        // 生成菜单数据返回数据
        return MenuTree.makeMenuTree(permissionList, -1L);
    }

    /**
     * 检查菜单是否有子菜单
     *
     * @param id
     * @return
     */
    @Override
    public boolean hasChildrenOfPermission(Long id) {
        // 创建条件构造器对象
        LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
        // 查询父级ID
        queryWrapper.eq(Permission::getParentId, id);
        // 判断数量是否大于0，如果大于0则表示存在
        return baseMapper.selectCount(queryWrapper) > 0;
    }

    /**
     * 查询分配权限树列表
     *
     * @param userId
     * @param roleId
     * @return
     */
    @Override
    public RolePermissionVo findPermissionTree(Long userId, Long roleId) {
        // 1.查询当前用户信息
        User user = userMapper.selectById(userId);
        List<PermissionVo> list = null;
        // 2.判断当前用户角色，如果是管理员，则查询所有权限，如果不是管理员，则只查询自己所拥有的权限
        if (!ObjectUtils.isEmpty(user.getIsAdmin()) && user.getIsAdmin() == BooleanStatus.TRUE) {
            // 查询所有权限
            list = baseMapper.findPermissionList(null);
        } else {
            // 根据用户ID查询
            list = baseMapper.findPermissionListByUserId(userId);
        }
        // 3.组装成树数据
        List<PermissionVo> permissionList = MenuTree.makeMenuTree(list, 0L);
        // 4.查询要分配角色的原有权限
        List<PermissionVo> rolePermission = baseMapper.findPermissionListByRoleId(roleId);
        // 5.找出该角色存在的数据
        List<Long> listIds = new ArrayList<>();
        Optional.ofNullable(list).orElse(new ArrayList<>())
                .stream()
                .filter(Objects::nonNull) //等同于 obj -> obj != null
                .forEach(item -> {
                    Optional.ofNullable(rolePermission).orElse(new ArrayList<>())
                            .stream()
                            .filter(Objects::nonNull)
                            .forEach(obj -> {
                                if (item.getId().equals(obj.getId())) {
                                    listIds.add(obj.getId());
                                    return;
                                }
                            });
                });
        // 创建
        RolePermissionVo vo = new RolePermissionVo();
        vo.setPermissionList(permissionList);
        vo.setCheckList(listIds.toArray());
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUserPermission(Long userId, List<Long> permissionIds) {
        List<Long> roleId = roleMapper.findRoleIdByUserId(userId); // 查询用户角色
        if (!ObjectUtils.isEmpty(roleId)) {
            // 保存用户角色权限
            roleMapper.saveRolePermission(roleId.get(0), permissionIds);
            return true;
        }
        return false;
    }

    @Override
    @Cacheable(value = CacheKeyConstants.PERMISSION, key = "#id", unless = "#result == null")
    public PermissionVo findPermissionById(Long id) {
        return this.convert(this.getById(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(PermissionVo permissionVo) {
        Permission permission = this.convert(permissionVo);
        if (this.save(permission)) {
            UserVo currentUser = this.getCurrentUser(); // 获取当前用户信息
            if (!ObjectUtils.isEmpty(currentUser)) {
                this.saveUserPermission(currentUser.getId(),
                        Collections.singletonList(permission.getId())); // 保存用户权限
            }
//            this.cachePermissionById(permission.getId());
            Objects.requireNonNull(cacheManager.getCache(CacheKeyConstants.PERMISSION)).clear();
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheKeyConstants.PERMISSION, key = "#permissionVo.id")
    public boolean update(PermissionVo permissionVo) {
        return this.updateById(this.convert(permissionVo));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheKeyConstants.PERMISSION, key = "#id")
    public boolean deleteById(Long id) {
        Objects.requireNonNull(cacheManager.getCache(CacheKeyConstants.PERMISSION)).clear();
        return this.removeById(id);
    }

    @CachePut(value = CacheKeyConstants.PERMISSION, key = "#result.id", condition = "#result != null")
    public PermissionVo cachePermissionById(Long id) {
        return this.convert(this.getById(id));
    }

    private Permission convert(PermissionVo permissionVo) {
        Permission permission = new Permission();
        BeanUtils.copyProperties(permissionVo, permission);
        return permission;
    }

    private PermissionVo convert(Permission permission) {
        PermissionVo permissionVo = new PermissionVo();
        BeanUtils.copyProperties(permission, permissionVo);
        return permissionVo;
    }

}
