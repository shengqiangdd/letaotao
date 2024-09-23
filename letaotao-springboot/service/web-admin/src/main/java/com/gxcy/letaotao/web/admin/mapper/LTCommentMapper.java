package com.gxcy.letaotao.web.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.gxcy.letaotao.common.entity.LTComment;
import com.gxcy.letaotao.common.enums.LTCommentType;
import com.gxcy.letaotao.web.admin.vo.LTCommentQueryVo;
import com.gxcy.letaotao.web.admin.vo.LTCommentVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 留言/评论表 Mapper 接口
 */
public interface LTCommentMapper extends BaseMapper<LTComment> {

    IPage<LTCommentVo> findListByPage(IPage<LTCommentVo> page,
                                      @Param(Constants.WRAPPER) QueryWrapper<LTCommentQueryVo> queryWrapper); // 分页查询留言/评论列表

    @Select("select count(id) from lt_comment where is_delete = 0 and reference_id = #{referenceId} and type = #{type.code}")
    int findCommentCount(Integer referenceId, LTCommentType type); // 通过referenceId和type查询评论数量

    @Delete("delete from lt_comment where reference_id = #{referenceId} and type = #{type.code}")
    void deleteByReferenceId(Integer referenceId, LTCommentType type); // 通过referenceId和type删除评论

}
