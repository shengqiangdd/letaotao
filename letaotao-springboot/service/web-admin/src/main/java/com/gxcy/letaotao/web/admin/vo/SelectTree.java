package com.gxcy.letaotao.web.admin.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SelectTree {

    private Object id;
    private Object value;
    private String label;
    private String imageUrl;

    private List<SelectTree> children;

}
