package com.gxcy.letaotao.web.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gxcy.letaotao.web.admin.entity.LTUser;
import com.gxcy.letaotao.web.admin.vo.LTUserQueryVo;
import com.gxcy.letaotao.web.admin.vo.LTUserVo;

/**
 * @Author csq
 * @Date 2024/9/14 18:55
 */
public interface LTUserService extends IService<LTUser> {
    /**
     * 根据昵称查询用户信息
     *
     * @param nickName
     * @return
     */
    LTUserVo findUserByNickName(String nickName);

    /**
     * 分页查询用户信息
     *
     * @param page
     * @param userQueryVo
     * @return
     */
    IPage<LTUserVo> findUserListByPage(IPage<LTUserVo> page, LTUserQueryVo userQueryVo);

    /**
     * 删除用户信息
     *
     * @return
     */
    boolean deleteById(Long id);

    LTUserVo findUserByWrapper(LTUserQueryVo userQueryVo);

    LTUserVo findUserById(Long id);

    boolean add(LTUserVo userVo);

    boolean update(LTUserVo userVo);
}
