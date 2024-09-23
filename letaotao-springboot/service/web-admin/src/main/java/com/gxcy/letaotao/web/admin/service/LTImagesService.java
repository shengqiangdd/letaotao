package com.gxcy.letaotao.web.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gxcy.letaotao.common.entity.LTImages;
import com.gxcy.letaotao.common.enums.LTImagesType;
import com.gxcy.letaotao.web.admin.dto.LTImagesDTO;
import com.gxcy.letaotao.web.admin.vo.LTImagesVo;

import java.util.List;

/**
 * 图片表 服务类
 */
public interface LTImagesService extends IService<LTImages> {

    List<LTImagesVo> getImagesList(Integer relatedId, LTImagesType type);

    boolean batchUpdate(List<LTImagesVo> ltImagesVos);

    boolean deleteById(Integer id);

    LTImagesVo upload(LTImagesDTO ltImagesDTO);

    boolean update(LTImagesVo ltImagesVo);
}
