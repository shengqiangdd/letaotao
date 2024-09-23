package com.gxcy.letaotao.web.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gxcy.letaotao.common.entity.LTLike;
import com.gxcy.letaotao.common.enums.LTLikeTargetType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

/**
 * 点赞表 Mapper 接口
 */
public interface LTLikeMapper extends BaseMapper<LTLike> {

    @Select("select count(id) from lt_like where is_active = 1 and target_id = #{targetId} and target_type = #{targetType.code}")
    int findLikeCount(Integer targetId, LTLikeTargetType targetType); // 通过目标ID和目标类型查询点赞数量

    @Delete("delete from lt_like where target_id = #{targetId} and target_type = #{targetType.code}")
    void deleteByTargetId(Integer targetId, LTLikeTargetType targetType); // 通过目标ID和目标类型删除点赞记录

}
