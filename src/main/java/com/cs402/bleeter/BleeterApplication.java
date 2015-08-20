package com.cs402.bleeter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.web.WebApplicationInitializer;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.cs402.bleeter" })
@Import({MongoFactoryConfig.class, SecurityConfiguration.class, MVCConfig.class})
public class BleeterApplication extends SpringBootServletInitializer implements WebApplicationInitializer{

    public static void main(String[] args) {
        SpringApplication.run(BleeterApplication.class, args);
    }
}
