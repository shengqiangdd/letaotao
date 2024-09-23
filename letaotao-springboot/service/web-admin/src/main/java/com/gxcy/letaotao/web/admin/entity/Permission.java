package com.gxcy.letaotao.web.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.gxcy.letaotao.common.enums.BooleanStatus;
import com.gxcy.letaotao.common.enums.PermissionType;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("sys_permission")
@Tag(name = "Permission对象", description = "")
public class Permission implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Parameter(name = "权限编号")
    @TableId(value = "id", type = IdType.AUTO)
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
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @Parameter(name = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @Parameter(name = "备注")
    private String remark;

    @Parameter(name = "排序")
    private Integer orderNum;

    @Parameter(name = "是否删除(0-未删除，1-已删除)")
    private BooleanStatus isDelete;

}
