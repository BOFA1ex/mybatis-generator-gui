package com.bofa.application;

import com.bofa.javafx.MainApp;
import com.sun.javafx.application.LauncherImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-08-25
 */
@MapperScan(basePackages = "com.bofa")
@SpringBootApplication(scanBasePackages = "com.bofa")
public class HelpApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(HelpApplication.class, args);
        LauncherImpl.launchApplication(MainApp.class, args);
    }
}
