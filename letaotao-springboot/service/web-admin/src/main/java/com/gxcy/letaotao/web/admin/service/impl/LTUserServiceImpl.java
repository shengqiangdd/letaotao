package com.gxcy.letaotao.web.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxcy.letaotao.common.config.redis.CacheKeyConstants;
import com.gxcy.letaotao.common.enums.LTImagesType;
import com.gxcy.letaotao.web.admin.entity.LTUser;
import com.gxcy.letaotao.web.admin.mapper.LTUserMapper;
import com.gxcy.letaotao.web.admin.service.LTImagesService;
import com.gxcy.letaotao.web.admin.service.LTUserService;
import com.gxcy.letaotao.web.admin.vo.LTImagesVo;
import com.gxcy.letaotao.web.admin.vo.LTUserQueryVo;
import com.gxcy.letaotao.web.admin.vo.LTUserVo;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @Author csq
 * @Date 2024/9/14 18:55
 */
@Service
public class LTUserServiceImpl extends ServiceImpl<LTUserMapper, LTUser> implements LTUserService {

    @Resource
    private LTUserMapper ltUserMapper;
    @Resource
    private LTImagesService ltImagesService;

    @Override
    @Cacheable(value = CacheKeyConstants.APP_USER, key = "#result.id", unless = "#result == null")
    public LTUserVo findUserByNickName(String nickName) {
        LambdaQueryWrapper<LTUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LTUser::getNickName, nickName);
        return ltUserMapper.findUserByWrapper(queryWrapper);
    }

    @Override
    @Cacheable(value = CacheKeyConstants.APP_USER, keyGenerator = "customKeyGenerator", unless = "#result == null")
    public IPage<LTUserVo> findUserListByPage(IPage<LTUserVo> page, LTUserQueryVo userQueryVo) {
        LambdaQueryWrapper<LTUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(userQueryVo.getNickName()), LTUser::getNickName, userQueryVo.getNickName());
        queryWrapper.eq(!ObjectUtils.isEmpty(userQueryVo.getGender()), LTUser::getGender, userQueryVo.getGender());
        return ltUserMapper.findListByPage(page, queryWrapper);
    }

    @Override
    public LTUserVo findUserByWrapper(LTUserQueryVo userQueryVo) {
        LambdaQueryWrapper<LTUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(userQueryVo.getNickName()), LTUser::getNickName, userQueryVo.getNickName());
        queryWrapper.eq(!ObjectUtils.isEmpty(userQueryVo.getId()), LTUser::getId, userQueryVo.getId());
        queryWrapper.eq(!ObjectUtils.isEmpty(userQueryVo.getGender()), LTUser::getGender, userQueryVo.getGender());
        return ltUserMapper.findUserByWrapper(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Cacheable(value = CacheKeyConstants.APP_USER, key = "#result.id", unless = "#result == null")
    public LTUserVo findUserById(Long id) {
        return this.convert(this.getById(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(LTUserVo userVo) {
        LTUser ltUser = this.convert(userVo);
        if (this.save(ltUser)) {
            this.cacheUserById(ltUser.getId());
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheKeyConstants.APP_USER, key = "#userVo.id")
    public boolean update(LTUserVo userVo) {
        return this.updateById(this.convert(userVo));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheKeyConstants.APP_USER, key = "#id")
    public boolean deleteById(Long id) {
        // 查询
        LTUser user = baseMapper.selectById(id);
        // 删除用户
        if (baseMapper.deleteById(id) > 0) {
            // 判断有无头像，有则删除
            if (user != null && !ObjectUtils.isEmpty(user.getAvatar())) {
                List<LTImagesVo> imagesList = ltImagesService.getImagesList(Math.toIntExact(user.getId()), LTImagesType.USER);
                for (LTImagesVo imagesVo : imagesList) {
                    ltImagesService.deleteById(imagesVo.getId());
                }
            }
            return true;
        }
        return false;
    }

    @CachePut(value = CacheKeyConstants.APP_USER, key = "#result.id", unless = "#result == null")
    public LTUserVo cacheUserById(Long id) {
        return this.convert(this.getById(id));
    }

    private LTUser convert(LTUserVo userVo) {
        LTUser ltUser = new LTUser();
        BeanUtils.copyProperties(userVo, ltUser);
        return ltUser;
    }

    private LTUserVo convert(LTUser ltUser) {
        LTUserVo ltUserVo = new LTUserVo();
        BeanUtils.copyProperties(ltUser, ltUserVo);
        return ltUserVo;
    }
}
