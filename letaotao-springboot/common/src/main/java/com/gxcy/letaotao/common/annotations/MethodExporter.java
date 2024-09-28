package com.gxcy.letaotao.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author csq
 * @Date 2024/9/28 20:25
 */
@Target(ElementType.METHOD) // 注解在方法上
@Retention(RetentionPolicy.RUNTIME) // 运行时生效
public @interface MethodExporter {

}
