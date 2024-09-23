package com.gxcy.letaotao.web.admin.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author csq
 * @Date 2024/9/15 17:54
 */
@Data
public class DictTreeVo {
    private Long id;
    private Object value;
    private String label;

    private List<DictTreeVo> children;
}
