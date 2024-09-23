package com.gxcy.letaotao.web.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.gxcy.letaotao.common.enums.BooleanStatus;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("sys_role")
@Tag(name = "Role对象", description = "")
public class Role implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Parameter(name = "角色编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Parameter(name = "角色编码")
    private String roleCode;

    @Parameter(name = "角色名称")
    private String roleName;

    @Parameter(name = "创建人")
    private Long createUser;

    @Parameter(name = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @Parameter(name = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @Parameter(name = "备注")
    private String remark;

    @Parameter(name = "是否删除(0-未删除，1-已删除)")
    private BooleanStatus isDelete;

}
