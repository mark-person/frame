package com.ppx.cloud.base.config.console;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.base.config.pojo.Config;
import com.ppx.cloud.base.config.pojo.ConfigResult;
import com.ppx.cloud.base.config.pojo.ConfigService;
import com.ppx.cloud.base.jdbc.page.Page;
import com.ppx.cloud.base.mvc.ControllerReturn;

@Controller
public class ConfigController {

	@Autowired
	private ConfigServImpl impl;
	
	public ModelAndView config(ModelAndView mv) {
		mv.addObject("data", list(new Page(), new Config()));
		return mv;
	}
	
	public Map<String, Object> list(Page page, Config pojo) {
		return ControllerReturn.page(page, impl.list(page, pojo));
	}
	
	public Map<String, Object> update(@RequestParam String configName, @RequestParam String configValue) {
        return impl.update(configName, configValue);
    }
	
	
	public ModelAndView configResult(ModelAndView mv, String configName) {
		ConfigResult configResult = new ConfigResult();
		configResult.setConfigName(configName);
		mv.addObject("configName", configName);
		mv.addObject("data", listConfigResult(new Page(), configResult));
		
		return mv;
	}
	
	public Map<?, ?> listConfigResult(Page page, ConfigResult pojo) {
		return ControllerReturn.page(page, impl.listConfigResult(page, pojo));
	}
	
	
	public ModelAndView configService(ModelAndView mv) {
        mv.addObject("data", listConfigService(new Page(), new ConfigService()));
        return mv;
    }
    
    public Map<String, Object> listConfigService(Page page, ConfigService pojo) {
        return ControllerReturn.page(page, impl.listConfigService(page, pojo));
    }
    
    public Map<String, Object> disableService(@RequestParam String serviceId) {
        return impl.disableService(serviceId);
    }
    
    public Map<String, Object> enableService(@RequestParam String serviceId) {
        return impl.enableService(serviceId);
    }
}
