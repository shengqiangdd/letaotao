package com.gxcy.letaotao.web.app.controller;


import com.gxcy.letaotao.common.utils.Result;
import com.gxcy.letaotao.web.app.service.LTCategoryService;
import com.gxcy.letaotao.web.app.vo.LTCategoryQueryVo;
import com.gxcy.letaotao.web.app.vo.SelectTree;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "分类控制器")
@RestController
@RequestMapping("/api/wx_categories")
public class LTWeChatCategoryController {

    @Resource
    private LTCategoryService ltCategoryService;

    /**
     * 查询分类树列表
     */
    @GetMapping("/tree")
    @Operation(summary = "查询分类树列表")
    public Result<?> getCategoryTreeList(@RequestParam(required = false) LTCategoryQueryVo categoryQueryVo) {
        // 调用查询分类树列表方法
        List<SelectTree> categoryTree = ltCategoryService.getCategoryTree(categoryQueryVo);
        // 返回数据
        return Result.ok(categoryTree);
    }

}
