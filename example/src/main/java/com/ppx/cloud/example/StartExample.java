package com.ppx.cloud.example;



import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.ppx.cloud.auth.config.AuthAllConfigExec;
import com.ppx.cloud.auth.config.AuthConfigExec;
import com.ppx.cloud.auth.config.AuthGrantConfigExec;
import com.ppx.cloud.base.config.ConfigExec;
import com.ppx.cloud.base.config.ConfigUtils;
import com.ppx.cloud.base.config.api.ConfigApiServ;
import com.ppx.cloud.base.config.pojo.Config;
import com.ppx.cloud.base.exception.ExceptionCode;
import com.ppx.cloud.base.exception.custom.ErrorException;
import com.ppx.cloud.monitor.config.MonitorSwitchConfigExec;
import com.ppx.cloud.monitor.config.MonitorThresholdConfigExec;


/**
 * 
 * @author mark
 * @date 2019年9月19日
 */
@Service
public class StartExample implements ApplicationListener<ContextRefreshedEvent> {
    
	private static Logger logger = LoggerFactory.getLogger(StartExample.class);
	
	@Autowired
    private ConfigApiServ configServ;
    
    @Value("${info.app.artifactId}")
    private String artifactId;
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event)  {
    	
    	logger.info("StartExample-config----------begin");
    	
    	// 绑定config_name 	varchar(64) not null comment '每个名称对应一个ConfigExec的实现类'
    	ConfigUtils.bindConfigExec("MONITOR_THRESHOLD", new MonitorThresholdConfigExec());
    	ConfigUtils.bindConfigExec("MONITOR_SWITCH", new MonitorSwitchConfigExec());
    	
    	ConfigUtils.bindConfigExec("AUTH_CONFIG", new AuthConfigExec());
    	ConfigUtils.bindConfigExec("AUTH_ALL", new AuthAllConfigExec());
    	ConfigUtils.bindConfigExec("AUTH_GRANT", new AuthGrantConfigExec());
    	
    	
    	List<Config> configList = configServ.listConfig(artifactId);
    	for (Config config : configList) {
			String configName = config.getConfigName();
			String configValue = config.getConfigValue();
			
			ConfigExec configExec = ConfigUtils.getConfigExec(configName);
			if (configExec == null) {
				throw new ErrorException(ExceptionCode.ERROR_CONFIG, configName + " not binding");
			}
			else if (Strings.isNotEmpty(configValue)) {
				configExec.run(configValue);
			}
		}
    	
    	configServ.initConfigService(artifactId);
    	
    	logger.info("StartExample-config----------end1");
    }
}
