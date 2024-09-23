package com.gxcy.letaotao.web.admin.controller;

import com.gxcy.letaotao.common.utils.Result;
import com.gxcy.letaotao.web.admin.service.DataDictionaryService;
import com.gxcy.letaotao.web.admin.vo.DataDictionaryQueryVo;
import com.gxcy.letaotao.web.admin.vo.DataDictionaryVo;
import com.gxcy.letaotao.web.admin.vo.DictTreeVo;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author csq
 * @Date 2024/9/15 15:21
 */
@Tag(name = "数据字典控制器")
@RestController
@RequestMapping("/api/dictionaries")
public class DataDictionaryController {

    @Resource
    private DataDictionaryService dataDictionaryService;

    /**
     * 查询数据字典列表
     *
     * @param dictionaryQueryVo
     * @return
     */
    @GetMapping("/list")
    public Result<?> getDictList(DataDictionaryQueryVo dictionaryQueryVo) {
        // 查询数据字典列表
        List<DataDictionaryVo> dataDictionaryVoList = dataDictionaryService.findDictList(dictionaryQueryVo);
        // 返回数据
        return Result.ok(dataDictionaryVoList);
    }

    @GetMapping("/list/code/{dictCode}")
    public Result<?> getDictListByDictCode(@PathVariable String dictCode) {
        // 查询数据字典列表
        List<DictTreeVo> dataDictionaryVoList = dataDictionaryService.getDictionaryList(dictCode);
        // 返回数据
        return Result.ok(dataDictionaryVoList);
    }

    // 导入数据字典
    @PostMapping("/importData")
    public Result<?> importDict(MultipartFile file) {
        dataDictionaryService.importDictData(file);
        return Result.ok().message("导入数据字典成功");
    }

    // 导出数据字典接口
    @GetMapping("/exportData")
    public void exportDict(HttpServletResponse response) {
        dataDictionaryService.exportDictData(response);
    }

}
