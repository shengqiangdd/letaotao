package com.gxcy.letaotao.web.app.vo;

import lombok.Data;

@Data
public class LTCategoryQueryVo {

    private String name;

    private Boolean hasImage;

    private Integer parentId;
}
