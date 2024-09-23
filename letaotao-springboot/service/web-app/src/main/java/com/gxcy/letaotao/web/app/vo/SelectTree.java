package com.gxcy.letaotao.web.app.vo;

import lombok.Data;

import java.util.List;

@Data
public class SelectTree {

    private Object id;
    private Object value;
    private String label;
    private String imageUrl;

    private List<SelectTree> children;

}
