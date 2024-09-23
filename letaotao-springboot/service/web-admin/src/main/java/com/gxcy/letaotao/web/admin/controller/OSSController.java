package com.gxcy.letaotao.web.admin.controller;

import com.gxcy.letaotao.common.utils.Result;
import com.gxcy.letaotao.web.admin.service.FileService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/oss/file")
public class OSSController {

    @Resource
    private FileService fileService;

    /**
     * 文件上传
     *
     * @param file
     * @param module
     * @return
     */
    @PostMapping("/upload")
    public Result<?> upload(MultipartFile file, String module) {
        // 返回上传到oss的路径
        String url = fileService.upload(file, module);
        return Result.ok(url).message("文件上传成功");
    }

    /**
     * 文件批量上传
     *
     * @param files
     * @param module
     * @return
     */
    @PostMapping("/multiUpload")
    public Result<?> multiUpload(MultipartFile[] files, String module) {
        // 返回上传到oss的路径
        List<String> urls = fileService.multiUpload(files, module);
        return Result.ok(urls).message("文件上传成功");
    }
}
