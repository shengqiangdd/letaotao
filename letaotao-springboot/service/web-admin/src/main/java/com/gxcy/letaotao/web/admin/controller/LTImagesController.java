package com.gxcy.letaotao.web.admin.controller;

import com.gxcy.letaotao.common.utils.Result;
import com.gxcy.letaotao.web.admin.dto.LTImagesDTO;
import com.gxcy.letaotao.web.admin.service.LTImagesService;
import com.gxcy.letaotao.web.admin.vo.LTImagesVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "图片控制器")
@RestController
@RequestMapping("/api/lt_images")
public class LTImagesController {

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
        // 上传图片信息
        LTImagesVo imagesVo = ltImagesService.upload(ltImagesDTO);
        return imagesVo != null ? Result.ok(imagesVo).message("文件上传成功")
                : Result.error().message("文件上传失败");
    }

    /**
     * 更新图片
     *
     * @param ltImagesVo
     * @return
     */
    @PostMapping("")
    @Operation(summary = "更新图片")
    public Result<?> update(@RequestBody LTImagesVo ltImagesVo) {
        // 更新图片信息
        boolean update = ltImagesService.update(ltImagesVo);
        return update ? Result.ok().message("图片更新成功") : Result.error().message("图片更新失败");
    }

    /**
     * 批量更新图片
     *
     * @param ltImagesVos
     * @return
     */
    @PostMapping("/batch")
    @Operation(summary = "批量更新图片")
    public Result<?> updateBatch(@RequestBody List<LTImagesVo> ltImagesVos) {
        boolean update = ltImagesService.batchUpdate(ltImagesVos);
        return update ? Result.ok().message("图片更新成功") : Result.error().message("图片更新失败");
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
        boolean delete = ltImagesService.deleteById(id);
        return delete ? Result.ok().message("图片删除成功") : Result.error().message("图片删除失败");
    }

}
