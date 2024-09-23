package com.gxcy.letaotao.web.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gxcy.letaotao.common.entity.LTComment;
import com.gxcy.letaotao.common.enums.LTCommentType;
import com.gxcy.letaotao.web.app.vo.LTWechatCommentQueryVo;
import com.gxcy.letaotao.web.app.vo.LTWechatCommentVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 留言/评论表 Mapper 接口
 */
public interface LTCommentMapper extends BaseMapper<LTComment> {

    List<LTWechatCommentVo> findList(LTWechatCommentQueryVo commentVO); // 微信端查询留言/评论列表

    @Select("select count(id) from lt_comment where is_delete = 0 and reference_id = #{referenceId} and type = #{type.code}")
    int findCommentCount(Integer referenceId, LTCommentType type); // 通过referenceId和type查询评论数量

    @Delete("delete from lt_comment where reference_id = #{referenceId} and type = #{type.code}")
    void deleteByReferenceId(Integer referenceId, LTCommentType type); // 通过referenceId和type删除评论

}
