package com.ppx.cloud.example.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import com.ppx.cloud.auth.common.AuthInterceptor;
import com.ppx.cloud.base.mvc.CommonInterceptor;
import com.ppx.cloud.base.mvc.CommonMvcConfig;
import com.ppx.cloud.base.util.ApplicationUtils;
import com.ppx.cloud.monitor.contoller.MonitorInterceptor;  



@Configuration
public class ExampleMvcConfig extends CommonMvcConfig {
	
    @Value("${spring.profiles.active}")
    private String active;

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		super.configureMessageConverters(converters);
	}
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		super.addViewControllers(registry);
	}	
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		super.addInterceptors(registry);
		registry.addInterceptor(new CommonInterceptor()).excludePathPatterns("/static/**/*", "/favicon.ico", "/img/**/*");
		registry.addInterceptor(new AuthInterceptor()).excludePathPatterns("/static/**/*", "/favicon.ico", "/img/**/*");
		registry.addInterceptor(new MonitorInterceptor()).excludePathPatterns("/static/**/*", "/favicon.ico", "/img/**/*");
	}
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        
        // jar路径的上一层
        ApplicationHome home = new ApplicationHome(ExampleMvcConfig.class);
       
        
        if ("dev".equals(active)) {
            ApplicationUtils.JAR_PARENT_HOME = home.getSource().getParentFile().getParentFile().getParentFile().getParent() + "/";
        }
        else {
            ApplicationUtils.JAR_PARENT_HOME = home.getSource().getParentFile().getParent() + "/";
        }
        
        registry.addResourceHandler("/img/**").addResourceLocations("file:" + ApplicationUtils.JAR_PARENT_HOME + "img/")
        	.setCachePeriod(Integer.MAX_VALUE);
    }
	
	
	
	
	
	
	
}  
