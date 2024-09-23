package com.gxcy.letaotao.web.admin.vo;

import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 生成菜单树
 */
public class MenuTree implements Serializable {

    /**
     * 生成路由
     *
     * @param menuList 菜单列表
     * @param pid      父级菜单
     * @return
     */
    public static List<RouterVo> makeRouter(List<PermissionVo> menuList, Long pid) {
        // 创建集合保存路由列表
        List<RouterVo> routerList = new ArrayList<>();
        // 如果menuList菜单列表不为空，则使用菜单列表，否则创建集合对象
        Optional.ofNullable(menuList).orElse(new ArrayList<PermissionVo>())
                // 筛选不为空的菜单及菜单父id相同的数据
                .stream().filter(item -> item != null && Objects.equals(item.getParentId(), pid))
                .forEach(item -> {
                    // 创建路由对象
                    RouterVo router = new RouterVo();
                    router.setName(item.getName()); // 路由名称
                    router.setPath(item.getPath()); // 路由地址
                    // 判断是否是一级菜单
                    if (item.getParentId() == 0L) {
                        router.setComponent("Layout"); // 一级菜单组件
                        router.setAlwaysShow(true); // 显示路由
                    } else {
                        router.setComponent(item.getUrl()); // 具体的组件
                        router.setAlwaysShow(false); // 折叠路由
                    }
                    // 设置meta信息
                    router.setMeta(router.new Meta(item.getLabel(), item.getIcon(),
                            item.getCode().split(",")));
                    // 递归生成路由
                    List<RouterVo> children = makeRouter(menuList, item.getId());
                    router.setChildren(children);//设置子路由到路由对象中
                    // 将路由信息添加到集合中
                    routerList.add(router);
                });
        return routerList;
    }

    /**
     * 生成菜单树
     *
     * @param menuList
     * @param pid
     * @return
     */
    public static List<PermissionVo> makeMenuTree(List<PermissionVo> menuList, Long pid) {
        // 创建集合保存菜单
        List<PermissionVo> permissionList = new ArrayList<>();
        // 如果menuLit菜单列表不为空，则使用菜单列表，否则创建集合对象
        Optional.ofNullable(menuList).orElse(new ArrayList<PermissionVo>())
                .stream().filter(item -> item != null
                        && Objects.equals(item.getParentId(), pid))
                .forEach(item -> {
                    // 创建菜单权限对象
                    PermissionVo permission = new PermissionVo();
                    // 复制属性
                    BeanUtils.copyProperties(item, permission);
                    // 获取每一个item的下级菜单，递归生成菜单树
                    List<PermissionVo> children = makeMenuTree(menuList, item.getId());
                    // 设置子菜单
                    permission.setChildren(children);
                    // 将菜单对象添加到集合
                    permissionList.add(permission);
                });
        return permissionList;
    }
}
