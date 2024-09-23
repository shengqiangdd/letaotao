package com.gxcy.letaotao.web.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gxcy.letaotao.web.app.dto.UserInfoDTO;
import com.gxcy.letaotao.web.app.dto.WeChatLoginDTO;
import com.gxcy.letaotao.web.app.entity.LTUser;
import com.gxcy.letaotao.web.app.vo.LTUserQueryVo;
import com.gxcy.letaotao.web.app.vo.LTUserVo;
import com.gxcy.letaotao.web.app.vo.UserInfoVo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface WeChatUserService extends BaseService<LTUser> {

    IPage<LTUserVo> findListByPage(IPage<LTUserVo> page, LTUserQueryVo userQueryVO);

    String getOpenIdByCode(String code);

    LTUserVo registerNewUser(UserInfoDTO userInfo, String openId);

    boolean updateUserInfo(UserInfoDTO userInfoDTO);

    LTUserVo findUserByOpenId(String openId);

    LTUserVo findUserById(Long id);

    void logout(HttpServletRequest request, HttpServletResponse response);

    LTUserVo getCurrentUser();

    boolean add(LTUserVo userVo);

    boolean update(LTUserVo userVo);

    boolean update(UserInfoDTO userInfoDTO);

    boolean deleteById(Long id);

    void login(WeChatLoginDTO weChatLoginDTO, HttpServletRequest request, HttpServletResponse response) throws IOException;

    UserInfoVo getUserInfo(Long userId);

    UserInfoVo findUserFollowCount(Long userId);
}
