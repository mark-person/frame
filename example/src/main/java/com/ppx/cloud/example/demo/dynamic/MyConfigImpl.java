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

/**
 * @author mark
 * @date 2020年2月11日
 */
@Component
public class MyConfigImpl implements BeanPostProcessor {  
	@Autowired
    private DefaultListableBeanFactory defaultListableBeanFactory;
	   
    @Override  
    public Object postProcessAfterInitialization(Object bean, String beanName)  
    throws BeansException {  
    	if (beanName.equals("myAppcation")) {  
            // do something  
        	System.out.println("xxxxxxx:myAppcationpostProcessAfterInitialization");
            return bean;  
        } 
        return bean;  
    }  
   
    @Override  
    public Object postProcessBeforeInitialization(Object bean, String beanName)  
    throws BeansException {  
        if (beanName.equals("myAppcationx")) { 
        	boolean containsBean = defaultListableBeanFactory.containsBean("myAppcation");
            if (containsBean) {
                //移除bean的定义和实例
                defaultListableBeanFactory.removeBeanDefinition("myAppcation");
            }
            //注册新的bean定义和实例
            Object obj = null;
            try {
           		defaultListableBeanFactory.registerBeanDefinition("myAppcation", BeanDefinitionBuilder.genericBeanDefinition(bean.getClass()).getBeanDefinition());
           		return bean;
            } catch (Exception e) {
				e.printStackTrace();
			}
           
            
            bean = null;
            
            return null;
        }  
        return bean;  
    } 
    
}  