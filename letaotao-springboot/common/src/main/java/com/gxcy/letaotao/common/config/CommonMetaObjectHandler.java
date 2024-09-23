package com.gxcy.letaotao.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 通用字段填充
 */
@Component
public class CommonMetaObjectHandler implements MetaObjectHandler {
    /**
     * 新增
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {

        //参数1：元数据对象
        //参数2：属性名称
        //参数3：类对象
        //参数4：当前系统时间
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "createTime", Timestamp.class, Timestamp.from(new Date().toInstant()));
        this.strictInsertFill(metaObject, "updateTime", Timestamp.class, Timestamp.from(new Date().toInstant()));
    }

    /**
     * 修改
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "updateTime", Timestamp.class, Timestamp.from(new Date().toInstant()));
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}
