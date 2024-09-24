package com.gxcy.letaotao.web.admin.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gxcy.letaotao.common.enums.BooleanStatus;
import com.gxcy.letaotao.common.enums.PermissionType;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author csq
 * @Date 2024/9/14 18:04
 */
@Data
@NoArgsConstructor
public class PermissionVo implements Serializable {
    @Parameter(name = "权限编号")
    private Long id;

    @Parameter(name = "权限名称")
    private String label;

    @Parameter(name = "父权限ID")
    private Long parentId;

    @Parameter(name = "父权限名称")
    private String parentName;

    @Parameter(name = "授权标识符")
    private String code;

    @Parameter(name = "路由地址")
    private String path;

    @Parameter(name = "路由名称")
    private String name;

    @Parameter(name = "授权路径")
    private String url;

    @Parameter(name = "权限类型(0-目录 1-菜单 2-按钮)")
    private PermissionType type;

    @Parameter(name = "图标")
    private String icon;

    @Parameter(name = "创建时间")
    private Date createTime;

    @Parameter(name = "修改时间")
    private Date updateTime;

    @Parameter(name = "备注")
    private String remark;

    @Parameter(name = "排序")
    private Integer orderNum;

    @Parameter(name = "是否删除(0-未删除，1-已删除)")
    private BooleanStatus isDelete;

    @Parameter(name = "子菜单列表")
    @JsonInclude(JsonInclude.Include.NON_NULL) // 属性值为null不进行序列化操作
    private List<PermissionVo> children = new ArrayList<>();

    @Parameter(name = "用于前端判断是菜单、目录或按钮")
    private String value;

    @Parameter(name = "是否展开")
    private Boolean open;
}
