package com.bofa.management.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-08-30
 */
@Component
public class ApplicationContextUtil implements ApplicationContextAware {

    static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    public static <T> T getBean(Class<T> kclass) {
        return applicationContext.getBean(kclass);
    }
}
