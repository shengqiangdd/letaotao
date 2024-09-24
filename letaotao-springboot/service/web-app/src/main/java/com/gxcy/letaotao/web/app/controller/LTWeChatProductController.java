package com.gxcy.letaotao.web.app.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gxcy.letaotao.common.utils.Result;
import com.gxcy.letaotao.web.app.dto.LTWeChatProductDTO;
import com.gxcy.letaotao.web.app.service.LTWeChatProductService;
import com.gxcy.letaotao.web.app.vo.LTWechatProductQueryVo;
import com.gxcy.letaotao.web.app.vo.LTWechatProductVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "商品控制器")
@RestController
@RequestMapping("/api/wx_products")
public class LTWeChatProductController {

    @Resource
    private LTWeChatProductService ltProductService;

    /**
     * 分页查询商品列表
     *
     * @param productQueryVo
     * @return
     */
    @GetMapping("/list/page")
    @Operation(summary = "分页查询商品列表")
    public Result<?> getProductPageList(LTWechatProductQueryVo productQueryVo) {
        // 创建分页对象
        IPage<LTWechatProductVo> page = new Page<>(productQueryVo.getPageNo(), productQueryVo.getPageSize());
        // 调用分页查询帖子列表方法
        ltProductService.findListByPage(page, productQueryVo);
        // 返回数据
        return Result.ok(page);
    }

    @GetMapping("/list/{user}/page")
    @Operation(summary = "分页查询用户的商品列表")
    public Result<?> getProductListByUserId(@PathVariable String user, LTWechatProductQueryVo productQueryVo) {
        // 创建分页对象
        IPage<LTWechatProductVo> page = new Page<>(productQueryVo.getPageNo(), productQueryVo.getPageSize());
        // 调用分页查询帖子列表方法
        ltProductService.findListPageByUserId(page, productQueryVo);
        // 返回数据
        return Result.ok(page);
    }

    @PostMapping("/save")
    @Operation(summary = "商品保存草稿")
    public Result<?> save(@Validated @RequestBody LTWeChatProductDTO productDTO) {
        // 调用保存商品方法
        int id = ltProductService.save(productDTO);
        return id > 0 ? Result.ok(id).message("保存成功") : Result.error().message("保存失败");
    }

    @PostMapping("/publish")
    @Operation(summary = "商品发布")
    public Result<?> publish(@Validated @RequestBody LTWeChatProductDTO productDTO) {
        // 调用发布商品方法
        int id = ltProductService.publish(productDTO);
        return id > 0 ? Result.ok(id).message("发布成功") : Result.error().message("发布失败");
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取商品详情")
    public Result<?> get(@PathVariable Integer id, @RequestParam(required = false) Long userId) {
        // 调用获取商品详情方法
        LTWechatProductVo product = ltProductService.getProductRelationsById(id, userId);
        return Result.ok(product);
    }

    @PutMapping("")
    @Operation(summary = "更新商品")
    public Result<?> update(@Validated @RequestBody LTWeChatProductDTO productDTO) {
        boolean update = ltProductService.update(productDTO);
        return update ? Result.ok().message("修改成功") : Result.error().message("修改失败");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除商品")
    public Result<?> delete(@PathVariable Integer id) {
        boolean delete = ltProductService.deleteById(id);
        return delete ? Result.ok().message("删除成功") : Result.error().message("删除失败");
    }

    @PutMapping("/withdraw")
    @Operation(summary = "下架商品")
    public Result<?> withdraw(@RequestBody LTWechatProductVo ltProduct) {
        // 调用下架商品方法
        boolean withdraw = ltProductService.withdraw(ltProduct);
        return withdraw ? Result.ok().message("下架成功") : Result.error().message("下架失败");
    }

    @PutMapping("/relist")
    @Operation(summary = "重新上架商品")
    public Result<?> relist(@RequestBody LTWechatProductVo ltProduct) {
        // 调用重新上架商品方法
        boolean relist = ltProductService.relist(ltProduct);
        return relist ? Result.ok().message("重新上架成功") : Result.error().message("重新上架失败");
    }

}
