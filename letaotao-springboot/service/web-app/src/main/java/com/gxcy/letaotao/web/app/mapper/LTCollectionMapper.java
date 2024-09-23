package com.gxcy.letaotao.web.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gxcy.letaotao.common.entity.LTCollection;
import com.gxcy.letaotao.common.enums.LTCollectionTargetType;
import com.gxcy.letaotao.web.app.vo.LTWechatCollectionQueryVo;
import com.gxcy.letaotao.web.app.vo.LTWechatCollectionVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 收藏表 Mapper 接口
 */
public interface LTCollectionMapper extends BaseMapper<LTCollection> {

    List<LTWechatCollectionVo> findProductList(LTWechatCollectionQueryVo ltWechatCollectionVO); // 收藏商品列表

    List<LTWechatCollectionVo> findPostList(LTWechatCollectionQueryVo ltWechatCollectionVO); // 收藏帖子列表

    @Select("select count(lc.id) from lt_collection lc where lc.`is_active` = 1 and lc.target_id = #{targetId} " +
            "and lc.target_type = #{targetType.code}")
    int findCollectionCount(Integer targetId, LTCollectionTargetType targetType); // 查询收藏数量

    @Select("select exists (select 1 from lt_collection lc where lc.`is_active` = 1 and lc.target_id = #{targetId} " +
            "and lc.target_type = #{targetType.code} and lc.user_id = #{userId})")
    boolean isCollection(Integer targetId, LTCollectionTargetType targetType, Long userId); // 通过目标ID和目标类型查询是否已收藏

    @Delete("delete from lt_collection where target_id = #{targetId} and target_type = #{targetType.code}")
    void deleteByTargetId(Integer targetId, LTCollectionTargetType targetType); // 根据目标ID和目标类型删除

    @Select("select `id`,user_id,target_id,target_type,create_time,is_active from lt_collection " +
            "where user_id = #{userId} and target_id = #{targetId} and target_type = #{targetType.code}")
    LTWechatCollectionVo findByUserIdAndTargetIdAndTargetType(Long userId, Integer targetId, LTCollectionTargetType targetType);

}
