package com.purple.purpleaicode;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.purple.purpleaicode.mapper")
public class PurpleAiCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PurpleAiCodeApplication.class, args);
    }

}
