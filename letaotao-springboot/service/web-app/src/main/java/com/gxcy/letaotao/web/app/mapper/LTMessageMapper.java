package com.gxcy.letaotao.web.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gxcy.letaotao.common.entity.LTMessage;
import org.apache.ibatis.annotations.Select;

/**
 * 通知/消息表 Mapper 接口
 */
public interface LTMessageMapper extends BaseMapper<LTMessage> {

    @Select("delete from lt_message where relation_id = #{relationId}")
    void deleteByRelationId(int relationId); // 删除关联的消息
}
