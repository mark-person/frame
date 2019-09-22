
package com.ppx.cloud.monitor.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * 监控配置
 * @author mark
 * @date 2018年7月2日
 */
@Configuration
public class MonitorSwitchConfig {
    

    public static boolean IS_DEBUG = false;
    
    public static boolean IS_WARNING = false;
    
    public static boolean IS_DEV = false;
    
    @Autowired
    private Environment env;
    
    @Bean
    public Object initMonitorConfig() {
        String active = env.getProperty("spring.profiles.active");
        IS_DEV = "dev".equals(active) ? true : false;
        return null;
    }
    
    public static void setMonitorProperties(boolean IS_DEBUG, boolean IS_WARNING) {
    	MonitorSwitchConfig.IS_DEBUG = IS_DEBUG;
    	MonitorSwitchConfig.IS_WARNING = IS_WARNING;
    }
    
}
