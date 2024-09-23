package com.gxcy.letaotao.web.admin.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class DictEeVo {

    @ExcelProperty(value = "id", index = 0)
    private Long id;

    @ExcelProperty(value = "上级id", index = 1)
    private Long parentId;

    @ExcelProperty(value = "名称", index = 2)
    private String name;

    @ExcelProperty(value = "字符值", index = 3)
    private String strValue;

    @ExcelProperty(value = "数字值", index = 4)
    private Integer numValue;

    @ExcelProperty(value = "编码", index = 5)
    private String dictCode;

}