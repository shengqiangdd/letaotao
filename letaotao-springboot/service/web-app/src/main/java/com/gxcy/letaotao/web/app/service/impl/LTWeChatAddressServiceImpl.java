package com.gxcy.letaotao.web.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxcy.letaotao.common.config.redis.CacheKeyConstants;
import com.gxcy.letaotao.common.entity.LTAddress;
import com.gxcy.letaotao.common.enums.BooleanStatus;
import com.gxcy.letaotao.web.app.mapper.LTAddressMapper;
import com.gxcy.letaotao.web.app.service.LTWeChatAddressService;
import com.gxcy.letaotao.web.app.vo.LTWechatAddressVo;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 收货地址表 服务实现类
 */
@Service
public class LTWeChatAddressServiceImpl extends ServiceImpl<LTAddressMapper, LTAddress> implements LTWeChatAddressService {

    @Resource
    private CacheManager cacheManager;

    @Override
    @Cacheable(value = CacheKeyConstants.ADDRESS, key = "#result.id", unless = "#result == null")
    public LTWechatAddressVo findDefaultAddress(Long userId) {
        return baseMapper.findDefaultAddress(userId);
    }

    @Override
    @Cacheable(value = CacheKeyConstants.ADDRESS, key = "#id", unless = "#result == null")
    public LTWechatAddressVo findAddressById(Integer id) {
        return this.convert(this.getById(id));
    }

    @Override
    @Cacheable(value = CacheKeyConstants.ADDRESS, key = "'list_'+userId", unless = "#result == null")
    public List<LTWechatAddressVo> findAddressList(Long userId) {
        LambdaQueryWrapper<LTAddress> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(userId != null && userId > 0, LTAddress::getUserId, userId);
        // 查询地址列表
        List<LTAddress> ltAddressList = baseMapper.selectList(wrapper);
        return this.convert(ltAddressList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(LTWechatAddressVo addressVo) {
        LambdaQueryWrapper<LTAddress> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LTAddress::getUserId, addressVo.getUserId());
        // 如果该用户没有默认地址，则设置该地址为默认地址
        long count = this.count(wrapper);
        if (count <= 0 && addressVo.getIsDefault() != BooleanStatus.TRUE) {
            addressVo.setIsDefault(BooleanStatus.TRUE);
        }
        LTAddress ltAddress = this.convert(addressVo);
        if(this.save(ltAddress)) {
            this.cacheAddressById(ltAddress.getId());
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheKeyConstants.ADDRESS, key = "#addressVo.id")
    public boolean update(LTWechatAddressVo addressVo) {
        return this.updateById(this.convert(addressVo));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheKeyConstants.ADDRESS, key = "#id")
    public boolean deleteById(Integer id) {
        LTAddress ltAddress = this.getById(id);
        if(this.removeById(id)) {
            Objects.requireNonNull(cacheManager
                    .getCache(CacheKeyConstants.ADDRESS)).evict("list_"+ltAddress.getUserId());
            return true;
        }
        return false;
    }

    @CachePut(value = CacheKeyConstants.ADDRESS, key = "#result.id", unless = "#result == null")
    public LTWechatAddressVo cacheAddressById(Integer id) {
        return this.convert(this.getById(id));
    }

    private LTAddress convert(LTWechatAddressVo addressVo) {
        LTAddress address = new LTAddress();
        BeanUtils.copyProperties(addressVo, address);
        return address;
    }

    private LTWechatAddressVo convert(LTAddress address) {
        LTWechatAddressVo addressVo = new LTWechatAddressVo();
        BeanUtils.copyProperties(address, addressVo);
        return addressVo;
    }

    private List<LTWechatAddressVo> convert(List<LTAddress> ltAddressList) {
        return ltAddressList.stream().map(this::convert).toList();
    }

}
