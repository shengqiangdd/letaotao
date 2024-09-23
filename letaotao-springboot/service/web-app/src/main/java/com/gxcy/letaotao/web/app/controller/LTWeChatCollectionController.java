package com.gxcy.letaotao.web.app.controller;

import com.gxcy.letaotao.common.enums.LTCollectionTargetType;
import com.gxcy.letaotao.common.utils.Result;
import com.gxcy.letaotao.web.app.service.LTWeChatCollectionService;
import com.gxcy.letaotao.web.app.service.WeChatUserService;
import com.gxcy.letaotao.web.app.vo.LTUserVo;
import com.gxcy.letaotao.web.app.vo.LTWechatCollectionQueryVo;
import com.gxcy.letaotao.web.app.vo.LTWechatCollectionVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "收藏控制器")
@RestController
@RequestMapping("/api/wx_collections")
public class LTWeChatCollectionController {

    @Resource
    private LTWeChatCollectionService ltCollectionService;
    @Resource
    private WeChatUserService userService;

    /**
     * 查询收藏列表
     *
     * @param ltWechatCollectionVO
     */
    @GetMapping("/list")
    @Operation(summary = "查询收藏列表")
    public Result<?> getCollectionList(LTWechatCollectionQueryVo ltWechatCollectionVO) {
        // 获取当前用户
        LTUserVo currentUser = userService.getCurrentUser();
        ltWechatCollectionVO.setUserId(currentUser.getId());
        // 根据类型查询当前用户的收藏列表
        if (ltWechatCollectionVO.getTargetType() == LTCollectionTargetType.PRODUCT) {
            return Result.ok(ltCollectionService.getProductList(ltWechatCollectionVO));
        } else {
            return Result.ok(ltCollectionService.getPostList(ltWechatCollectionVO));
        }
    }

    /**
     * 添加或取消收藏
     *
     * @param ltCollectionVo
     * @return
     */
    @PostMapping("")
    @Operation(summary = "添加或取消收藏")
    public Result<?> addOrUpdate(@RequestBody LTWechatCollectionVo ltCollectionVo) {
        int status = ltCollectionService.addOrUpdate(ltCollectionVo);
        return Result.ok(status).message("收藏操作成功");
    }

    @DeleteMapping("/cancel")
    public Result<?> batchCancel(@RequestBody List<Integer> ids) {
        ltCollectionService.deleteByIds(ids);
        return Result.ok().message("取消收藏成功");
    }

}