package com.gxcy.letaotao.web.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gxcy.letaotao.common.entity.LTImages;
import com.gxcy.letaotao.common.enums.LTImagesType;
import com.gxcy.letaotao.web.admin.vo.LTImagesVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 图片表 Mapper 接口
 */
public interface LTImagesMapper extends BaseMapper<LTImages> {

    @Select("select i.`id`,i.url from lt_images i where i.related_id = #{relatedId} and i.`type` = #{type.code}")
    List<LTImagesVo> getImagesList(Integer relatedId, LTImagesType type); // 获取图片列表

}
