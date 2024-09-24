package com.gxcy.letaotao.web.admin.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用于给用户分配角色时保存选中的角色数据
 */
@Data
@NoArgsConstructor
public class UserRoleDTO {
    private Long userId;
    private List<Long> roleIds;
}
