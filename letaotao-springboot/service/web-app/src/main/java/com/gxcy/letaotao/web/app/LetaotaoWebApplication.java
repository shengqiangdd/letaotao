package com.gxcy.letaotao.web.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.gxcy.letaotao")
@MapperScan("com.gxcy.letaotao.web.app.mapper")
@SpringBootApplication
public class LetaotaoWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(LetaotaoWebApplication.class, args);
    }
}
