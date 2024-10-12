package com.gxcy.letaotao.web.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.gxcy.letaotao")
@MapperScan("com.gxcy.letaotao.web.admin.mapper")
@RefreshScope
@SpringBootApplication
public class LetaotaoAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(LetaotaoAdminApplication.class, args);
    }
}
