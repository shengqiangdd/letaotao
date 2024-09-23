package com.gxcy.letaotao.web.app.controller;

import com.gxcy.letaotao.common.entity.LTImages;
import com.gxcy.letaotao.common.utils.Result;
import com.gxcy.letaotao.web.app.dto.LTImagesDTO;
import com.gxcy.letaotao.web.app.service.LTImagesService;
import com.gxcy.letaotao.web.app.vo.LTImagesVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "图片控制器")
@RestController
@RequestMapping("/api/lt_images")
public class LTWechatImagesController {

    @Resource
    private LTImagesService ltImagesService;

    /**
     * 文件上传
     *
     * @param ltImagesDTO
     * @return
     */
    @PostMapping("/upload")
    public Result<?> upload(LTImagesDTO ltImagesDTO) {
        LTImagesVo imagesVo = ltImagesService.upload(ltImagesDTO);
        if (imagesVo != null) {
            return Result.ok(imagesVo).message("文件上传成功");
        }
        return Result.error().message("文件上传失败");
    }

    /**
     * 更新图片
     *
     * @param ltImages
     * @return
     */
    @PutMapping("")
    @Operation(summary = "更新图片")
    public Result<?> update(@RequestBody LTImages ltImages) {
        // 更新图片信息
        if (ltImagesService.updateById(ltImages)) {
            return Result.ok().message("图片更新成功");
        } else {
            return Result.error().message("图片更新失败");
        }
    }

    /**
     * 批量更新图片
     *
     * @param ltImagesVos
     * @return
     */
    @PutMapping("/batch")
    @Operation(summary = "批量更新图片")
    public Result<?> updateBatch(@RequestBody List<LTImagesVo> ltImagesVos) {
        if (ltImagesService.batchUpdate(ltImagesVos)) {
            return Result.ok().message("图片更新成功");
        } else {
            return Result.error().message("图片更新失败");
        }
    }

    /**
     * 删除图片
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除图片")
    public Result<?> delete(@PathVariable Integer id) {
        if (ltImagesService.deleteById(id)) {
            return Result.ok().message("图片删除成功");
        } else {
            return Result.error().message("图片删除失败");
        }
    }

}
