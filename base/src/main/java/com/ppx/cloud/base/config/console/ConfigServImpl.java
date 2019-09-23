package com.ppx.cloud.base.config.console;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.ContextAttributes.Impl;
import com.ppx.cloud.base.config.ConfigExec;
import com.ppx.cloud.base.config.ConfigUtils;
import com.ppx.cloud.base.config.api.ConfigApiServ;
import com.ppx.cloud.base.config.pojo.Config;
import com.ppx.cloud.base.config.pojo.ConfigResult;
import com.ppx.cloud.base.config.pojo.ConfigService;
import com.ppx.cloud.base.jdbc.MyDaoSupport;
import com.ppx.cloud.base.jdbc.page.Page;
import com.ppx.cloud.base.mvc.ControllerReturn;
import com.ppx.cloud.base.util.ApplicationUtils;

@Service
public class ConfigServImpl extends MyDaoSupport {
	
	
	@Autowired
	private ConfigApiServ configApiServ;
	
	public List<Config> list(Page page, Config pojo) {
	    
		var cSql = new StringBuilder("select count(*) from base_config_value");
		var qSql = new StringBuilder("select * from base_config_value order by config_name");
		
		List<Config> list = queryForPage(Config.class, page, cSql, qSql);
		return list;
	}
	
	@Transactional
	public Map<String, Object> update(String configName, String configValue) {
		try {
			new ObjectMapper().readValue(configValue, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
			return ControllerReturn.error(30000, "configValue不是json格式:" + e.getMessage());
		}
		
		// 更新本地
		ConfigExec configExec = ConfigUtils.getConfigExec(configName);
		String r = configExec.run(configValue);
		if (Strings.isNotEmpty(r)) {
			return ControllerReturn.error(30001, "更新出错:" + r);
		}
		
		String updateSql = "update base_config_value set config_value = ?, modified = now() where config_name = ?";
		getJdbcTemplate().update(updateSql, configValue, configName);
		
		// 清除执行结果
        String deleteSql = "delete from base_config_result where config_name = ?";
        getJdbcTemplate().update(deleteSql, configName);
		String insertSql = "insert into base_config_result(config_name, service_id, exec_result) values(?, ?, ?)";
		// 更新成功记录
		getJdbcTemplate().update(insertSql, configName, ApplicationUtils.getServiceId(), 1);
		
		Map<String, Object> syncMap = configApiServ.callSync(configName, configValue);
		int totalNum = (Integer)syncMap.get("totalNum");
		int successNum = (Integer)syncMap.get("successNum");
		
		String updataStatusSql = "update base_config_value set config_status = ? where config_name = ?";
		if (totalNum == 0) {
			getJdbcTemplate().update(updataStatusSql, "all", configName);
			return ControllerReturn.of("msg", "本服务刷新成功", "configValue", configValue);
		} else if (totalNum != successNum) {
			getJdbcTemplate().update(updataStatusSql, "part", configName);
			String msg = (String)syncMap.get("msg");
			return ControllerReturn.of("msg", "共" + (totalNum + 1) + "个服务, " + (totalNum - successNum) + "刷新失败, 失败原因:" + msg, "configValue", configValue);
		}
		return ControllerReturn.of("msg", (totalNum + 1) + "个服务全部刷新成功", "configValue", configValue);
	}

	public List<ConfigResult> listConfigResult(Page page, ConfigResult pojo) {
		
		var c = createCriteria("where").addAnd("config_name = ?", pojo.getConfigName())
				.addAnd("exec_result = ?", pojo.getExecResult());
		

		var cSql = new StringBuilder("select count(*) from base_config_result").append(c);
		var qSql = new StringBuilder("select * from base_config_result").append(c).append(" order by modified desc");
		
		List<ConfigResult> list = queryForPage(ConfigResult.class, page, cSql, qSql, c.getParaList());
		return list;
	}
	
	public List<ConfigService> listConfigService(Page page, ConfigService pojo) {
        
        var cSql = new StringBuilder("select count(*) from base_config_service");
        var qSql = new StringBuilder("select * from base_config_service");
        
        List<ConfigService> list = queryForPage(ConfigService.class, page, cSql, qSql);
        return list;
    }
    
	public Map<String, Object> disableService(@RequestParam String serviceId) {
	    String sql = "update base_config_service set service_status = 0 where service_id = ?";
	    getJdbcTemplate().update(sql, serviceId);
	    return ControllerReturn.of("serviceStatus", 0);
	}
	
	public Map<String, Object> enableService(@RequestParam String serviceId) {
        String sql = "update base_config_service set service_status = 1 where service_id = ?";
        getJdbcTemplate().update(sql, serviceId);
        return ControllerReturn.of("serviceStatus", 1);
    }
	
	public Map<String, Object> reRequest(String configName, String serviceId) {
		String valueSql = "select config_value from base_config_value where config_name = ?";
		String configValue = getJdbcTemplate().queryForObject(valueSql, String.class, configName);
		
		return configApiServ.reRequest(serviceId, configName, configValue);
		
	}
}
