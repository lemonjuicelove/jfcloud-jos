package com.github.jfcloud.jos;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.github.jfcloud.jos.mapper")
public class JfcloudJosApplication {

    public static void main(String[] args) {
        SpringApplication.run(com.github.jfcloud.jos.JfcloudJosApplication.class, args);
    }

}
