package com.gxcy.letaotao.web.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxcy.letaotao.common.config.redis.CacheKeyConstants;
import com.gxcy.letaotao.common.entity.LTMessage;
import com.gxcy.letaotao.web.admin.mapper.LTMessageMapper;
import com.gxcy.letaotao.web.admin.service.LTMessageService;
import com.gxcy.letaotao.web.admin.vo.LTMessageQueryVo;
import com.gxcy.letaotao.web.admin.vo.LTMessageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 通知/消息表 服务实现类
 */
@Service
public class LTMessageServiceImpl extends ServiceImpl<LTMessageMapper, LTMessage> implements LTMessageService {

    @Override
    @Cacheable(value = CacheKeyConstants.MESSAGE, keyGenerator = "customKeyGenerator", unless = "#result == null")
    public IPage<LTMessageVo> findListByPage(IPage<LTMessageVo> page, LTMessageQueryVo messageQueryVo) {
        //创建条件构造器对象
        QueryWrapper<LTMessageQueryVo> queryWrapper = new QueryWrapper<>();

        //通知/消息内容
        queryWrapper.like(!ObjectUtils.isEmpty(messageQueryVo.getContent()),
                "m.content", messageQueryVo.getContent());
        //通知/消息类型（1：通知，2：消息）
        queryWrapper.eq(!ObjectUtils.isEmpty(messageQueryVo.getType())
                        && messageQueryVo.getType().getCode() > 0,
                "m.message_type", messageQueryVo.getType());
        //用户昵称
        if (!ObjectUtils.isEmpty(messageQueryVo.getNickName())) {
            queryWrapper.likeRight("su.nick_name", messageQueryVo.getNickName())
                    .or().likeRight("ru.nick_name", messageQueryVo.getNickName());
        }
        //通知/消息发送时间
        queryWrapper.like(!ObjectUtils.isEmpty(messageQueryVo.getSendTime()),
                "m.create_time", messageQueryVo.getSendTime());

        //查询并返回数据
        return baseMapper.findListByPage(page, queryWrapper);
    }

    @Override
    @Cacheable(value = CacheKeyConstants.MESSAGE, key = "#id", unless = "#result == null")
    public LTMessageVo findMessageById(Integer id) {
        return this.convert(this.getById(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(LTMessageVo messageVo) {
        LTMessage message = this.convert(messageVo);
        if (this.save(message)) {
            this.cacheMessageById(message.getId());
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheKeyConstants.MESSAGE, key = "#messageVo.id")
    public boolean update(LTMessageVo messageVo) {
        LTMessage message = this.convert(messageVo);
        return this.updateById(message);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheKeyConstants.MESSAGE, key = "#id")
    public boolean deleteById(Integer id) {
        return this.removeById(id);
    }

    @CachePut(value = CacheKeyConstants.MESSAGE, key = "#result.id")
    public LTMessageVo cacheMessageById(Integer id) {
        return this.convert(this.getById(id));
    }

    private LTMessage convert(LTMessageVo messageVo) {
        LTMessage message = new LTMessage();
        BeanUtils.copyProperties(messageVo, message);
        return message;
    }

    private LTMessageVo convert(LTMessage message) {
        LTMessageVo messageVo = new LTMessageVo();
        BeanUtils.copyProperties(message, messageVo);
        return messageVo;
    }
}
