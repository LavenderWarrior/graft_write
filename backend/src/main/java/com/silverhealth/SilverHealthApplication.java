package com.silverhealth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.silverhealth.mapper")
public class SilverHealthApplication {
    public static void main(String[] args) {
        SpringApplication.run(SilverHealthApplication.class, args);
    }
}
