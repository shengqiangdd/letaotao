package com.gxcy.letaotao.web.admin.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxcy.letaotao.common.config.redis.CacheKeyConstants;
import com.gxcy.letaotao.web.admin.config.listener.DictListener;
import com.gxcy.letaotao.web.admin.entity.DataDictionary;
import com.gxcy.letaotao.web.admin.mapper.DataDictionaryMapper;
import com.gxcy.letaotao.web.admin.service.DataDictionaryService;
import com.gxcy.letaotao.web.admin.vo.DataDictionaryQueryVo;
import com.gxcy.letaotao.web.admin.vo.DataDictionaryVo;
import com.gxcy.letaotao.web.admin.vo.DictEeVo;
import com.gxcy.letaotao.web.admin.vo.DictTreeVo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author csq
 * @description 针对表【data_dictionary】的数据库操作Service实现
 * @createDate 2024-09-15 13:48:52
 */
@Service
public class DataDictionaryServiceImpl extends ServiceImpl<DataDictionaryMapper, DataDictionary>
        implements DataDictionaryService {

    @Resource
    private DataDictionaryMapper dataDictionaryMapper;

    /**
     * 查询字典列表
     *
     * @param dictionaryQueryVo
     * @return
     */
    @Override
    @Cacheable(value = CacheKeyConstants.DICTIONARY, keyGenerator = "customKeyGenerator", unless = "#result == null")
    public List<DataDictionaryVo> findDictList(DataDictionaryQueryVo dictionaryQueryVo) {
        QueryWrapper<DataDictionary> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(dictionaryQueryVo.getDictCode()),
                "d.dict_code", dictionaryQueryVo.getDictCode());
        queryWrapper.eq(StringUtils.isNotEmpty(dictionaryQueryVo.getName()),
                "d.`name`", dictionaryQueryVo.getName());
        queryWrapper.eq(ObjectUtils.isNotEmpty(dictionaryQueryVo.getId()),
                "d.`id`", dictionaryQueryVo.getId());
        // 查询所有字典项
        List<DataDictionaryVo> dictList = baseMapper.findDictList(queryWrapper);
        // 将查询结果按 ID 和父节点 ID 分组
        Map<Long, DataDictionaryVo> idMap = dictList.stream()
                .collect(Collectors.toMap(DataDictionaryVo::getId, item -> item));
        // 找到根节点（没有父节点的）
        List<DataDictionaryVo> rootNodes = dictList.stream()
                .filter(item -> item.getParentId() == 0 || !idMap.containsKey(item.getParentId()))
                .collect(Collectors.toList());
        // 构建树形结构
        buildChildren(rootNodes, idMap);
        return rootNodes;
    }

    //导出数据字典接口
    @Override
    public void exportDictData(HttpServletResponse response) {
        //设置下载信息
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        // 设置 Content-Disposition 为 UTF-8 编码的文件名
        String fileName = "dict";
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + fileName + ".xlsx");
        //查询数据库
        List<DataDictionary> dictList = baseMapper.selectList(null);
        //Dict -- DictEeVo
        List<DictEeVo> dictVoList = new ArrayList<>();
        for (DataDictionary dict : dictList) {
            DictEeVo dictEeVo = new DictEeVo();
            BeanUtils.copyProperties(dict, dictEeVo);
            dictVoList.add(dictEeVo);
        }
        //调用方法进行写操作
        try {
            EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet("dict")
                    .doWrite(dictVoList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //导入数据字典
    @Override
    @CacheEvict(value = CacheKeyConstants.DICTIONARY, allEntries = true)
    public void importDictData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), DictEeVo.class, new DictListener(baseMapper)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据字典编码获取字典列表
     *
     * @param dictCode 字典编码
     * @return 字典列表
     */
    @Override
    @Cacheable(value = CacheKeyConstants.DICTIONARY, keyGenerator = "customKeyGenerator", unless = "#result == null")
    public List<DictTreeVo> getDictionaryList(String dictCode) {
        // 查询所有字典项
        DataDictionaryVo dictionaryVo = dataDictionaryMapper.selectByDictCode(dictCode);

        LambdaQueryWrapper<DataDictionary> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataDictionary::getParentId, dictionaryVo.getId());
        List<DataDictionary> dataDictionaryList = baseMapper.selectList(queryWrapper);

        return buildDictListVo(dataDictionaryList);
    }

    /**
     * 递归构建子节点
     *
     * @param list  当前层级的节点列表
     * @param idMap 所有节点的 ID 映射
     */
    private void buildChildren(List<DataDictionaryVo> list, Map<Long, DataDictionaryVo> idMap) {
        for (DataDictionaryVo entity : list) {
            Long id = entity.getId();
            if (idMap.containsKey(id)) {
                // 从 idMap 中获取当前节点，并设置其子节点
                List<DataDictionaryVo> children = idMap.values().stream()
                        .filter(subItem -> subItem.getParentId() != null && subItem.getParentId().equals(id))
                        .collect(Collectors.toList());
                entity.setChildren(children);
                buildChildren(children, idMap);
            }
        }
    }

    private List<DictTreeVo> buildDictListVo(List<DataDictionary> list) {
        List<DictTreeVo> dictTreeVoList = new ArrayList<>();
        for (DataDictionary entity : list) {
            DictTreeVo dictTreeVo = new DictTreeVo();
            dictTreeVo.setId(entity.getId());
            dictTreeVo.setValue(ObjectUtils.isNotEmpty(entity.getStrValue()) ? entity.getStrValue() : entity.getNumValue());
            dictTreeVo.setLabel(entity.getName());
            dictTreeVoList.add(dictTreeVo);
        }
        return dictTreeVoList;
    }

}




