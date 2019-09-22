package com.ppx.cloud.base.config.api;

import java.util.Map;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import com.ppx.cloud.base.config.ConfigExec;
import com.ppx.cloud.base.config.ConfigUtils;
import com.ppx.cloud.base.mvc.ControllerReturn;

@Controller
public class ConfigApiController {

	
	// 接收来自操作服务的请求
	public Map<String, Object> sync(@RequestParam String configName, String configValue) {
		ConfigExec configRun = ConfigUtils.getConfigExec(configName);
		if (configRun == null) {
			return ControllerReturn.error(30001, "configParam:" + configName + "没有绑定ConfigRun");
		}
		else {
			String r = configRun.run(configValue);
			if (Strings.isNotEmpty(r)) {
				return ControllerReturn.error(30002, "configParam:" + configName + "执行失败，原因:" + r);
			}
		}
		return ControllerReturn.of();
	}
	
}
