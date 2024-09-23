package com.gxcy.letaotao.web.admin.vo;

import lombok.Data;

@Data
public class LTCategoryQueryVo {

    private Long pageNo = 1L; // 当前页码
    private Long pageSize = 10L; // 每页显示数量

    private String name;

    private Boolean hasImage;

    private Integer parentId;
}
