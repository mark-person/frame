package com.ppx.cloud.base.config.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.ppx.cloud.base.config.pojo.Config;
import com.ppx.cloud.base.jdbc.MyDaoSupport;
import com.ppx.cloud.base.util.ApplicationUtils;

@Service
public class ConfigApiServImpl extends MyDaoSupport implements ConfigApiServ {

	public List<Config> listConfig(String artifactId) {
		String sql = "select * from base_config_value where artifact_id = ?";
		List<Config> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Config.class), artifactId);
		return list;
	}
	
	public void initConfigService(String artifactId) {
	    String sql = "insert into base_config_service(service_id, artifact_id) values(?, ?) on duplicate key update service_status = 1";
	    getJdbcTemplate().update(sql, ApplicationUtils.getServiceId(), artifactId);
	}
	
    // 请求
    @SuppressWarnings("rawtypes")
    public Map<String, Object> callSync(String configName, String configValue) {
    	Map<String, Object> returnMap = new HashMap<String, Object>();
    	String msg = "";
    	
    	// 请求其它服务
    	String uri = "base/configApi/sync";
    	
    	
    	MultiValueMap<String, String> paramMap= new LinkedMultiValueMap<String, String>();
    	paramMap.add("configName", configName);
    	paramMap.add("configValue", configValue);
    	
    	RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(paramMap, new HttpHeaders());
    	
    	String currentServiceId = ApplicationUtils.getServiceId();
    	// 更新其它服务
		String otherSql = "select service_id from base_config_service where service_status = 1 and service_id != ? and " + 
				" artifact_id = (select artifact_id from base_config_value where config_name = ?)";
		List<String> ohterServiceIdList = getJdbcTemplate().queryForList(otherSql, String.class,  currentServiceId ,configName);
		
        int successNum = 0;
        String insertSql = "insert into base_config_result(config_name, service_id, exec_result, exec_desc) values(?, ?, ?, ?)";
		for (String serviceId : ohterServiceIdList) {
			try {
				String url = "http://" + serviceId + "/" + uri;
		        ResponseEntity<Map> resultMap = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
				int code = (Integer)resultMap.getBody().get("code");
				if (code == 0) {
					successNum++;
					// 更新成功记录
					getJdbcTemplate().update(insertSql, configName, serviceId, 1, null);
				}
				else {
				    msg += "接口:" + serviceId + "异常:" +  resultMap.getBody().get("msg") + ";";
					getJdbcTemplate().update(insertSql, configName, serviceId, 0, resultMap.getBody().get("msg"));
				}
			} catch (Exception e) {
				msg += "连接" + serviceId + "异常:" +  e.getMessage() + ";";
				getJdbcTemplate().update(insertSql, configName, serviceId, 0, e.getMessage());
			}
		}
		
		returnMap.put("successNum", successNum);
		returnMap.put("totalNum", ohterServiceIdList.size());
		returnMap.put("msg", msg);
    	return returnMap;
    }
    
    
    
}
