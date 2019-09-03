package com.bofa.application;

import com.bofa.codegen.mybatis.MybatisGenerateCodeSv;
import com.bofa.codegen.mybatis.dto.MybatisGenerateCodeRequest;
import com.bofa.javafx.MainApp;
import com.sun.javafx.application.LauncherImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

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
//        MybatisGenerateCodeRequest request = new MybatisGenerateCodeRequest();
//        request.setDbId(22L);
//        request.setEntityPrimaryKey("userId");
//        request.setEntityPrimaryKeyRule("GEN");
//        request.setTableName("user");
//        request.setEntityPackage("com.bofa.entity");
//        request.setDaoBasePath("/Users/Bofa/mybatis-generator-gui/mybatis-generator-service/src/main/java");
//        request.setDaoPackage("com.bofa.mapper");
//        context.getBean(MybatisGenerateCodeSv.class).generateMybatis(request);
    }
}
