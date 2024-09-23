package com.gxcy.letaotao.web.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gxcy.letaotao.common.entity.LTProduct;
import com.gxcy.letaotao.web.app.dto.LTWeChatProductDTO;
import com.gxcy.letaotao.web.app.vo.LTWechatProductQueryVo;
import com.gxcy.letaotao.web.app.vo.LTWechatProductVo;

public interface LTWeChatProductService extends IService<LTProduct> {

    IPage<LTWechatProductVo> findListByPage(IPage<LTWechatProductVo> page, LTWechatProductQueryVo productQueryVo);

    IPage<LTWechatProductVo> findListPageByUserId(IPage<LTWechatProductVo> page, LTWechatProductQueryVo productQueryVo);

    int publish(LTWeChatProductDTO productDTO);

    int save(LTWeChatProductDTO productDTO);

    boolean withdraw(LTWechatProductVo ltProduct);

    boolean relist(LTWechatProductVo ltProduct);

    LTWechatProductVo getProductRelationImagesById(Integer id);

    LTWechatProductVo getProductRelationsById(Integer id, Long userId);

    LTWechatProductVo findProductById(Integer id);

    boolean add(LTWechatProductVo productVo);

    boolean add(LTWeChatProductDTO productDTO);

    boolean update(LTWechatProductVo productVo);

    boolean update(LTWeChatProductDTO productDTO);

    boolean update(LTProduct ltProduct);

    boolean deleteById(Integer id);
}
