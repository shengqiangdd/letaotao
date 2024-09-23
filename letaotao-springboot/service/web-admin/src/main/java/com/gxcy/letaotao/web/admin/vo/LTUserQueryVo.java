package com.gxcy.letaotao.web.admin.vo;

import com.gxcy.letaotao.common.enums.GenderType;
import lombok.Data;

/**
 * 用户查询条件
 */
@Data
public class LTUserQueryVo {

    private Long pageNo = 1L;//当前页码
    private Long pageSize = 10L;//每页显示数量

    private Long id;
    private String nickName;
    private GenderType gender;

}
