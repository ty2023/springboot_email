package com.yj.springboot_email;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.yj.dao")
@SpringBootApplication(scanBasePackages = "com.yj")
public class SpringbootEmailApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootEmailApplication.class, args);
    }

}
