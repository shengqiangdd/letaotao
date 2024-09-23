package com.gxcy.letaotao.web.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.gxcy.letaotao")
@MapperScan("com.gxcy.letaotao.web.admin.mapper")
@SpringBootApplication
public class LetaotaoAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(LetaotaoAdminApplication.class, args);
    }
}
