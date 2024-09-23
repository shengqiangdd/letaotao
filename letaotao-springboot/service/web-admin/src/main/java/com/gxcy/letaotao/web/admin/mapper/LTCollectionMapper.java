package com.gxcy.letaotao.web.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gxcy.letaotao.common.entity.LTCollection;
import com.gxcy.letaotao.common.enums.LTCollectionTargetType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

/**
 * 收藏表 Mapper 接口
 */
public interface LTCollectionMapper extends BaseMapper<LTCollection> {

    @Select("select count(lc.id) from lt_collection lc where lc.`is_active` = 1 and lc.target_id = #{targetId} " +
            "and lc.target_type = #{targetType.code}")
    int findCollectionCount(Integer targetId, LTCollectionTargetType targetType); // 通过目标ID和目标类型查询是否已收藏

    @Select("select exists (select 1 from lt_collection lc where lc.`is_active` = 1 and lc.target_id = #{targetId} " +
            "and lc.target_type = #{targetType.code} and lc.user_id = #{userId})")
    boolean isCollection(Integer targetId, LTCollectionTargetType targetType, Long userId); // 通过目标ID和目标类型查询是否已收藏

    @Delete("delete from lt_collection where target_id = #{targetId} and target_type = #{targetType.code}")
    void deleteByTargetId(Integer targetId, LTCollectionTargetType targetType); // 根据目标ID和目标类型删除

}
