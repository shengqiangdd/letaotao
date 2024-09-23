package com.gxcy.letaotao.web.admin.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouterVo implements Serializable {
    @Parameter(name = "路由地址")
    private String path; // 路由地址

    @Parameter(name = "路由对应的组件")
    private String component; // 路由对应的组件

    @Parameter(name = "是否显示")
    private boolean alwaysShow; // 是否显示

    @Parameter(name = "路由名称")
    private String name; // 路由名称

    @Parameter(name = "路由meta信息")
    private Meta meta; // 路由meta信息

    @Data
    @AllArgsConstructor
    public class Meta {
        @Parameter(name = "标题")
        private String title; // 标题

        @Parameter(name = "图标")
        private String icon; // 图标

        @Parameter(name = "角色列表")
        private Object[] roles; // 角色列表
    }

    @Parameter(name = "子路由")
    private List<RouterVo> children = new ArrayList<>(); // 子路由
}
