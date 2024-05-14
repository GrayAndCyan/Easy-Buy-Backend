package com.mizore.easybuy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "com.mizore.easybuy.mapper")
public class EasyBuyApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyBuyApplication.class, args);
    }

}


