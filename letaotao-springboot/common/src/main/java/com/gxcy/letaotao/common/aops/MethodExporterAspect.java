package com.gxcy.letaotao.common.aops;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gxcy.letaotao.common.annotations.MethodExporter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @Author csq
 * @Date 2024/9/28 20:27
 */
@Aspect
@Component
@Slf4j
public class MethodExporterAspect {

    @Resource
    private ObjectMapper objectMapper;

    // 拦截所有带MethodExporter注解的方法
    @Around("@annotation(com.gxcy.letaotao.common.annotations.MethodExporter)")
    public Object methodExporter(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object retValue = null;

        long startTime = System.currentTimeMillis();
        
        retValue = proceedingJoinPoint.proceed(); // 执行目标方法
        
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        
        // 获得重载后的方法名
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();

        // 确定方法名后获得该方法上面配置的注解标签
        MethodExporter methodExporter = method.getAnnotation(MethodExporter.class);

        if (methodExporter != null) {
            // 获得方法里面的形参
            StringBuffer params = new StringBuffer();
            Object[] args = proceedingJoinPoint.getArgs();
            DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
            String[] parameterNames = discoverer.getParameterNames(method);
            for (int i = 0; i < args.length; i++) {
                params.append(Objects.requireNonNull(parameterNames)[i])
                        .append("\t").append(args[i].toString()).append(";");
            }
            // 将返回结果retValue序列化
            String jsonResult = null;
            if (retValue != null) {
                jsonResult = objectMapper.writeValueAsString(retValue);
            } else {
                jsonResult = "null";
            }

            log.info("""
                    
                    方法分析上报中
                    类名方法名：{}.{}()
                    执行耗时：{}毫秒
                    输入参数：{}
                    返回结果：{}
                    over
                    
                    """, method.getDeclaringClass().getName(), method.getName(),
                    totalTime, params, jsonResult);
        }

        return retValue;
    }
}
