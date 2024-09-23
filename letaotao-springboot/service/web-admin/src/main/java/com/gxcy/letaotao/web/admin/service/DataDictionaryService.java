package com.gxcy.letaotao.web.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gxcy.letaotao.web.admin.entity.DataDictionary;
import com.gxcy.letaotao.web.admin.vo.DataDictionaryQueryVo;
import com.gxcy.letaotao.web.admin.vo.DataDictionaryVo;
import com.gxcy.letaotao.web.admin.vo.DictTreeVo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author csq
 * @description 针对表【data_dictionary】的数据库操作Service
 * @createDate 2024-09-15 13:48:52
 */
public interface DataDictionaryService extends IService<DataDictionary> {

    List<DictTreeVo> getDictionaryList(String dictCode);

    List<DataDictionaryVo> findDictList(DataDictionaryQueryVo dictionaryQueryVo);

    //导出数据字典接口
    void exportDictData(HttpServletResponse response);

    //导入数据字典
    void importDictData(MultipartFile file);
}
