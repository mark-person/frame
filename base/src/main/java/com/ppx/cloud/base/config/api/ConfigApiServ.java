/**
 * 
 */
package com.ppx.cloud.base.config.api;

import java.util.List;
import java.util.Map;

import com.ppx.cloud.base.config.pojo.Config;

/**
 * @author mark
 * @date 2019年1月14日
 */
public interface ConfigApiServ {
    
	List<Config> listConfig(String artifactId);
	
	void initConfigService(String artifactId);
	
	Map<String, Object> callSync(String configName, String configValue);
}
