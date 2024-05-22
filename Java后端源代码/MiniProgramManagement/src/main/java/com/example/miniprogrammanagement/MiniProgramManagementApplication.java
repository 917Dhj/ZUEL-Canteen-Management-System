package com.example.miniprogrammanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class MiniProgramManagementApplication extends SpringBootServletInitializer {

    // 解决tomcat报错
    public MiniProgramManagementApplication() {
        super();
        //下面设置为false
        setRegisterErrorPageFilter(false);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MiniProgramManagementApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(MiniProgramManagementApplication.class, args);
    }
}
