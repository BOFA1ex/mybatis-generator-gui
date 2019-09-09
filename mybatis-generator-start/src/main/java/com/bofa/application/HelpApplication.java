package com.bofa.application;

import com.bofa.javafx.MainApp;
import com.sun.javafx.application.LauncherImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-08-25
 */
@MapperScan(basePackages = "com.bofa")
@SpringBootApplication(scanBasePackages = "com.bofa", exclude = FlywayAutoConfiguration.class)
public class HelpApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(HelpApplication.class, args);
        LauncherImpl.launchApplication(MainApp.class, args);
//        UserQueryReq req = new UserQueryReq();
//        req.setUsername("小");
//        List<User> list = context.getBean(UserSv.class).list(req);
//        list.forEach(System.out::println);
    }
}
