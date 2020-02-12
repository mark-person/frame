/**
 * 
 */
package com.ppx.cloud.example.demo.dynamic;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author mark
 * @date 2020年2月11日
 */
// @Component
public class LocalProcessor implements BeanPostProcessor {
 
    @Autowired
    private DefaultListableBeanFactory defaultListableBeanFactory;
 
 
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        boolean containsBean = defaultListableBeanFactory.containsBean(beanName);
        if (containsBean) {
            //移除bean的定义和实例
            defaultListableBeanFactory.removeBeanDefinition(beanName);
        }
        //注册新的bean定义和实例
        defaultListableBeanFactory.registerBeanDefinition(beanName, BeanDefinitionBuilder.genericBeanDefinition(bean.getClass()).getBeanDefinition());
        return bean;
    }
 
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
    
    
}