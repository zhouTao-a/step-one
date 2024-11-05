package com.cloud.springboot.beanPostProcessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class AllenBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // userController哈哈，我是bean实例化之后的结果
        if(beanName.equals("userController")) {
            System.out.println("----------------我是bean实例化之前的结果" +beanName + "----------------");
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(beanName.equals("userController")) {
            System.out.println("----------------我是bean实例化之后的结果" +beanName + "----------------");
        }

        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
