package com.gxcy.letaotao.web.admin.config.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gxcy.letaotao.web.admin.entity.DataDictionary;
import com.gxcy.letaotao.web.admin.mapper.DataDictionaryMapper;
import com.gxcy.letaotao.web.admin.vo.DictEeVo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

public class DictListener extends AnalysisEventListener<DictEeVo> {

    private final DataDictionaryMapper dictMapper;

    public DictListener(DataDictionaryMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    //一行一行读取
    @Override
    public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
        //调用方法添加数据库
        if (dictEeVo != null) {
            DataDictionary dict = new DataDictionary();
            LambdaQueryWrapper<DataDictionary> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(StringUtils.isNotEmpty(dictEeVo.getDictCode()), DataDictionary::getDictCode, dictEeVo.getDictCode());
            queryWrapper.eq(DataDictionary::getName, dictEeVo.getName());
            queryWrapper.eq(StringUtils.isNotEmpty(dictEeVo.getStrValue()), DataDictionary::getStrValue, dictEeVo.getStrValue());
            queryWrapper.eq(ObjectUtils.isNotEmpty(dictEeVo.getNumValue()), DataDictionary::getNumValue, dictEeVo.getNumValue());
            queryWrapper.eq(ObjectUtils.isNotEmpty(dictEeVo.getParentId()), DataDictionary::getParentId, dictEeVo.getParentId());
            if (dictMapper.selectOne(queryWrapper) != null && dictMapper.selectById(dictEeVo.getId()) != null) { // 判断数据库中是否有重复数据
                throw new RuntimeException("数据重复，请检查");
            }
            BeanUtils.copyProperties(dictEeVo, dict);
            dictMapper.insert(dict);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}