package com.gxcy.letaotao.web.app.vo;

import com.gxcy.letaotao.common.enums.UserFollowType;
import lombok.Data;

/**
 * 用户查询条件
 */
@Data
public class LTUserQueryVo {

    private Long pageNo = 1L;//当前页码
    private Long pageSize = 10L;//每页显示数量
    private String label;
    private Long userId;
    private UserFollowType followType;
}
