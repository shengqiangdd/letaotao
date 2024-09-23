package com.gxcy.letaotao.web.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.gxcy.letaotao.web.admin.entity.DataDictionary;
import com.gxcy.letaotao.web.admin.vo.DataDictionaryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author csq
 * @description 针对表【data_dictionary】的数据库操作Mapper
 * @createDate 2024-09-15 13:48:52
 * @Entity com.gxcy.letaotao.web.admin.entity.DataDictionary
 */
public interface DataDictionaryMapper extends BaseMapper<DataDictionary> {

    List<DataDictionaryVo> findDictList(@Param(Constants.WRAPPER) QueryWrapper<DataDictionary> dataDictionaryVo);

    /**
     * 根据字典编码查询所有字典项
     *
     * @param dictCode 字典编码
     * @return 字典项列表
     */
    DataDictionaryVo selectByDictCode(@Param("dictCode") String dictCode);

}




