package com.gxcy.letaotao.web.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.gxcy.letaotao.common.entity.LTMessage;
import com.gxcy.letaotao.web.admin.vo.LTMessageQueryVo;
import com.gxcy.letaotao.web.admin.vo.LTMessageVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 通知/消息表 Mapper 接口
 */
public interface LTMessageMapper extends BaseMapper<LTMessage> {

    IPage<LTMessageVo> findListByPage(IPage<LTMessageVo> page,
                                      @Param(Constants.WRAPPER) QueryWrapper<LTMessageQueryVo> queryWrapper); // 分页查询通知/消息

    @Select("delete from lt_message where relation_id = #{relationId}")
    void deleteByRelationId(int relationId); // 删除关联的消息
}
