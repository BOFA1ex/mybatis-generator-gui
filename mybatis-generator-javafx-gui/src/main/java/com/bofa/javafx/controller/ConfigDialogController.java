package com.bofa.javafx.controller;

import com.bofa.management.service.datasource.DbSv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author bofa1ex
 * @since 2019-08-30
 */
public class ConfigDialogController extends AbstractFxmlController {

    static final Logger logger = LoggerFactory.getLogger(ConfigDialogController.class);
    AbstractFxmlController mainController;

    /**
     * 尝试过自动装配，但是需要定义类变量instance且借助postConstruct获取容器对象，以及initialize来反转装配对象的属性
     * 方法可行，但是不如直接借助ApplicationContext获取bean对象，手动注入来的思路更利于理解
     */
    private DbSv dbSv;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setMainController(AbstractFxmlController mainController) {
        this.mainController = mainController;
    }
}
