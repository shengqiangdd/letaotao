package com.gxcy.letaotao.web.app.service;


import com.baomidou.mybatisplus.extension.service.IService;

public interface BaseService<T> extends IService<T> {

    T findByCondition(String condition, String value);

}
