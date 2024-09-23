package com.gxcy.letaotao.web.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gxcy.letaotao.common.entity.LTProduct;
import com.gxcy.letaotao.web.admin.vo.LTProductQueryVo;
import com.gxcy.letaotao.web.admin.vo.LTProductVo;

import java.util.List;

/**
 * 商品表 服务类
 */
public interface LTProductService extends IService<LTProduct> {

    IPage<LTProductVo> findListByPage(IPage<LTProductVo> page, LTProductQueryVo productQueryVo);

    LTProductVo findProductById(Integer id);

    boolean add(LTProductVo productVo);

    boolean update(LTProductVo productVo);

    boolean batchUpdate(List<LTProductVo> ltProductVos);

    boolean deleteById(Integer id);
}
