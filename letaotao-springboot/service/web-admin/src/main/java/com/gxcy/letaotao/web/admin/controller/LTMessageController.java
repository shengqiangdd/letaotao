package com.gxcy.letaotao.web.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxcy.letaotao.common.annotations.MethodExporter;
import com.gxcy.letaotao.common.utils.Result;
import com.gxcy.letaotao.web.admin.service.LTMessageService;
import com.gxcy.letaotao.web.admin.vo.LTMessageQueryVo;
import com.gxcy.letaotao.web.admin.vo.LTMessageVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "通知/消息控制器")
@RestController
@RequestMapping("/api/messages")
public class LTMessageController {

    @Resource
    private LTMessageService ltMessageService;

    /**
     * 分页查询通知/消息列表
     *
     * @param messageQueryVo
     * @return
     */
    @GetMapping("/list/page")
    @Operation(summary = "分页查询通知/消息列表")
    @MethodExporter
    public Result<?> getMessageList(LTMessageQueryVo messageQueryVo) {
        // 创建分页对象
        IPage<LTMessageVo> page = new Page<>(messageQueryVo.getPageNo(), messageQueryVo.getPageSize());
        // 调用分页查询评论列表方法
        ltMessageService.findListByPage(page, messageQueryVo);
        return Result.ok(page);
    }


    /**
     * 根据id查询通知/消息信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据id查询通知/消息信息")
    public Result<?> getMessageById(@PathVariable Integer id) {
        return Result.ok(ltMessageService.findMessageById(id));
    }

    /**
     * 删除通知/消息
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除通知/消息")
    @PreAuthorize("hasAuthority('ltt:message:delete')")
    public Result<?> delete(@PathVariable Integer id) {
        boolean delete = ltMessageService.deleteById(id);
        return delete ? Result.ok().message("通知/消息删除成功")
                : Result.error().message("通知/消息删除失败");
    }
}