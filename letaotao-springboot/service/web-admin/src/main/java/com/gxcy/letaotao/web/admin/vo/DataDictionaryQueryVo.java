package com.gxcy.letaotao.web.admin.vo;

import lombok.Data;

/**
 * @Author csq
 * @Date 2024/9/15 15:12
 */
@Data
public class DataDictionaryQueryVo {
    private Long id;

    /**
     * 编码
     */
    private String dictCode;

    private String name;
}
