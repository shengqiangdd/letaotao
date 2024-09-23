package com.gxcy.letaotao.web.admin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.gxcy.letaotao.web.admin.vo.UserVo;

public interface BaseService<T> extends IService<T> {
    UserVo getCurrentUser();

    T findByCondition(String condition, String value);

}
