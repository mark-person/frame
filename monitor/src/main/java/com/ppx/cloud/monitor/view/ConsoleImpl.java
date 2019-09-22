package com.ppx.cloud.monitor.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.util.Strings;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.ppx.cloud.base.jdbc.MyCriteria;
import com.ppx.cloud.base.jdbc.MyDaoSupport;
import com.ppx.cloud.base.jdbc.page.Page;
import com.ppx.cloud.base.mvc.ObjectMapperCustomer;

@Service
public class ConsoleImpl extends MyDaoSupport {
    
    public List<Map<String, Object>> listIndexService() {
        String sql = "select * from monitor_service where service_display = 1 order by service_ordinal";
        List<Map<String, Object>> returnList =  getJdbcTemplate().queryForList(sql);
        
        for (Map<String, Object> map : returnList) {
            String service_info = (String)map.get("service_info");
            map.put("service_info",  ObjectMapperCustomer.toJsonNode(service_info));
            
            String service_last_info = (String)map.get("service_last_info");
            map.put("service_last_info",  ObjectMapperCustomer.toJsonNode(service_last_info));
        }
        return returnList;
    }
	
	public List<Map<String, Object>> listDisplayService() {
		String sql = "select service_id from monitor_service where service_display = 1 order by service_ordinal";
		return getJdbcTemplate().queryForList(sql);
	}

	public List<Map<String, Object>> listAllService() {
		String sql = "select * from monitor_service order by service_ordinal";
	
		List<Map<String, Object>> returnList = getJdbcTemplate().queryForList(sql);
		
		for (Map<String, Object> map : returnList) {
            String startup_info = (String)map.get("service_info");
            map.put("service_info",  ObjectMapperCustomer.toJsonNode(startup_info));
            
            String service_last_info = (String)map.get("service_last_info");
            map.put("service_last_info",  ObjectMapperCustomer.toJsonNode(service_last_info));
        }
		return returnList;
	}
	
	public void display(String serviceId, int display) {
		String sql = "update monitor_service set service_display = ? where service_id = ?";
		getJdbcTemplate().update(sql, display, serviceId);
	}
	
	public void orderService(List<String> serviceId) {
		String updateSql = "update monitor_service set service_ordinal = ? where service_id = ?";
		List<Object[]> paraList = new ArrayList<Object[]>();
		for (int i = 0; i < serviceId.size(); i++) {
			Object[] obj = {i, serviceId.get(i)};
			paraList.add(obj);
		}
		getJdbcTemplate().batchUpdate(updateSql, paraList);
	}
	
	public List<Map<String, Object>> listStartup(Page page, String serviceId) {
		MyCriteria c = createCriteria("where").addAnd("service_id = ?", serviceId);
		
		StringBuilder cSql = new StringBuilder("select count(*) from monitor_startup").append(c);
		StringBuilder qSql = new StringBuilder("select * from monitor_startup").append(c).append("order by startup_id desc");
		
		List<Map<String, Object>> returnList = queryForPage(page, cSql, qSql, c.getParaList());
		
		for (Map<String, Object> map : returnList) {
            String startup_info = (String)map.get("startup_info");
            map.put("startup_info",  ObjectMapperCustomer.toJsonNode(startup_info));
        }
		return returnList;
	}

	
	public List<Map<String, Object>> listAccess(Page page, String date, String beginTime, String endTime,
			String serviceId, String uriText) {
		MyCriteria c = createCriteria("and").addAnd("a.access_time >= ?", beginTime)
				.addAnd("a.access_time <= ?", endTime)
				.addAnd("a.service_id = ?", serviceId)
				.addAnd("m.uri_text = ?", uriText);
		
		c.addPrePara(date);
		
		StringBuilder cSql = new StringBuilder("select count(*) from monitor_access a where a.access_date = ?").append(c);
		StringBuilder qSql = new StringBuilder("select * from monitor_access a left join monitor_uri_seq_map m "
				+ " on m.uri_seq = a.uri_seq where a.access_date = ?").append(c).append("order by access_id desc");
		
		
		List<Map<String, Object>> returnList = queryForPage(page, cSql, qSql, c.getParaList());
		
		for (Map<String, Object> map : returnList) {
            String access_info = (String)map.get("access_info");
            map.put("access_info",  ObjectMapperCustomer.toJsonNode(access_info));
        }
		return returnList;
		
		
	}
	
	public Map<String, Object> getAccess(String accessId) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
//		var sql = "select a.*, s.uriText, l.marker, l.log from access a"
//				+ " left join map_uri_seq s on s.uriSeq = a.uriSeq "
//				+ " left join access_log l on l.accessId = a.accessId where a.accessId = ?";
//		try (LogTemplate t = new LogTemplate()) {
//			returnMap = fetchOne(t, sql, accessId);
//		}
		return returnMap;
	}
	
	public List<Map<String, Object>> listError(Page page, String date, String beginTime, String endTime, String serviceId) {

		
		MyCriteria c = createCriteria("where").addAnd("a.access_time >= ?", beginTime)
                .addAnd("a.access_time <= ?", endTime)
                .addAnd("a.service_id = ?", serviceId);
        
        c.addPrePara(date);
        
        StringBuilder cSql = new StringBuilder("select count(*) from monitor_error e join monitor_access a on e.access_id = a.access_id and a.access_date = ?").append(c);
        StringBuilder qSql = new StringBuilder("select e.*, m.uri_text, a.access_info from monitor_error e" + 
                " join monitor_access a on e.access_id = a.access_id and a.access_date = ?" + 
                " left join monitor_uri_seq_map m on m.uri_seq = a.uri_seq").append(c).append("order by access_id desc");
        
        List<Map<String, Object>> returnList = queryForPage(page, cSql, qSql, c.getParaList());
        
        for (Map<String, Object> map : returnList) {
            String access_info = (String)map.get("access_info");
            map.put("access_info",  ObjectMapperCustomer.toJsonNode(access_info));
        }
        return returnList;
	}
	
	public Map<String, Object> getError(String accessId) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
//		var sql = "select from error where accessId = ?";
//		try (LogTemplate t = new LogTemplate()) {
//			returnMap = fetchOne(t, sql, accessId);
//		}
		return returnMap;
	}
	
	public List<Map<String, Object>> listGather(Page page, String serviceId) {
		
		MyCriteria c = createCriteria("where").addAnd("g.service_id = ?", serviceId);
        
        StringBuilder cSql = new StringBuilder("select count(*) from monitor_gather g").append(c);
        StringBuilder qSql = new StringBuilder("select * from monitor_gather g").append(c).append("order by gather_time desc");
        
        
        List<Map<String, Object>> returnList = queryForPage(page, cSql, qSql, c.getParaList());
        
        for (Map<String, Object> map : returnList) {
            String gather_info = (String)map.get("gather_info");
            map.put("gather_info",  ObjectMapperCustomer.toJsonNode(gather_info));
        }
        return returnList;
	}
	
	public List<Map<String, Object>> listStatUri(Page page, String uri) {
	    
		MyCriteria c = createCriteria("where").addAnd("u.uri = ?",  uri);
        
        StringBuilder cSql = new StringBuilder("select count(*) from monitor_stat_uri u").append(c);
        StringBuilder qSql = new StringBuilder("select u.*, m.uri_text from monitor_stat_uri u" + 
                 " left join monitor_uri_seq_map m on m.uri_seq = u.uri_seq").append(c).append("order by lasted desc");
        
        List<Map<String, Object>> returnList = queryForPage(page, cSql, qSql, c.getParaList());
        
        for (Map<String, Object> map : returnList) {
            String max_detail = (String)map.get("max_detail");
            map.put("max_detail",  ObjectMapperCustomer.toJsonNode(max_detail));
        }
		
		return returnList;
	}
	
	public List<Map<String, Object>> listStatSql(Page page, String sql) {
		MyCriteria c = createCriteria("where").addAnd("s.sql_md5 = ?",  sql);
        
        StringBuilder cSql = new StringBuilder("select count(*) from monitor_stat_sql s").append(c);
        StringBuilder qSql = new StringBuilder("select s.*, m.sql_text from monitor_stat_sql s "
              + "left join monitor_sql_md5_map m on m.sql_md5 = s.sql_md5").append(c).append("order by lasted desc");
        
        List<Map<String, Object>> returnList = queryForPage(page, cSql, qSql, c.getParaList());
        
        for (Map<String, Object> map : returnList) {
            String max_detail = (String)map.get("max_detail");
            map.put("max_detail",  ObjectMapperCustomer.toJsonNode(max_detail));
        }
        
        return returnList;
		
	}
	
	public List<Map<String, Object>> listStatResponse(Page page, String serviceId) {
		
		MyCriteria c = createCriteria("where").addAnd("r.service_id = ?",  serviceId);
        
        StringBuilder cSql = new StringBuilder("select count(*) from monitor_stat_response r").append(c);
        StringBuilder qSql = new StringBuilder("select r.* from monitor_stat_response r").append(c).append("order by hh desc");
        
        
        List<Map<String, Object>> returnList = queryForPage(page, cSql, qSql, c.getParaList());
        
		return returnList;
	}
	
	public List<Map<String, Object>> listStatWarning(Page page, String uriText) {

		MyCriteria c = createCriteria("where").addAnd("w.uri_seq = (select uri_seq from monitor_uri_seq_map where uri_text = ?)",  uriText);
        
        StringBuilder cSql = new StringBuilder("select count(*) from monitor_stat_warning w").append(c);
        StringBuilder qSql = new StringBuilder("select w.*, m.uri_text from monitor_stat_warning w left join monitor_uri_seq_map m"
        		+ " on w.uri_seq = m.uri_seq").append(c).append("order by w.lasted desc");
        
        
        List<Map<String, Object>> returnList = queryForPage(page, cSql, qSql, c.getParaList());
        
        return returnList;
	}
	
	public List<Map<String, Object>> listDebug(Page page, String serviceId, String date, String beginTime, String endTime,
			String uri, String marker) {
		MyCriteria c = new MyCriteria("where");
		c.addAnd("d.service_id = ?", serviceId);
		c.addAnd("d.debug_time >= ?", Strings.isEmpty(beginTime) ? null : date + " " + beginTime);
		c.addAnd("d.debug_time <= ?", Strings.isEmpty(endTime) ? null : date + " " + endTime);
		c.addAnd("m.uri_text = ?", uri);
		c.addAnd("exists(select 1 from monitor_access_log where access_id = d.access_id and marker = ?)", marker);
		
		if (Strings.isEmpty(beginTime) && Strings.isEmpty(endTime) && Strings.isNotEmpty(date)) {
			c.addAnd("d.debug_time >= ?", date + " 00:00:00");
			c.addAnd("d.debug_time <= ?", date + " 23:59:59");
		}
        
        StringBuilder cSql = new StringBuilder("select count(*) from monitor_debug d"
        		+ " left join monitor_access a on d.access_id = a.access_id"
				+ " left join monitor_uri_seq_map m on a.uri_seq = m.uri_seq").append(c);
        StringBuilder qSql = new StringBuilder("select d.*, m.uri_text, a.access_info, a.spend_time,"
        		+ " (select group_concat(l.marker) from monitor_access_log l where l.access_id = d.access_id) marker"
				+ " from monitor_debug d"
				+ " left join monitor_access a on d.access_id = a.access_id"
				+ " left join monitor_uri_seq_map m on a.uri_seq = m.uri_seq").append(c).append("order by debug_time desc");
        
        List<Map<String, Object>> returnList = queryForPage(page, cSql, qSql, c.getParaList());
        
        for (Map<String, Object> map : returnList) {
        	String access_info = (String)map.get("access_info");
            map.put("access_info",  ObjectMapperCustomer.toJsonNode(access_info));
            
            String debug_info = (String)map.get("debug_info");
            map.put("debug_info",  ObjectMapperCustomer.toJsonNode(debug_info));
        }
		return returnList;
	}
	
	public List<String> listDebugSql(String accessId) {
		List<String> returnList = new ArrayList<String>();
		
		String sql = "select json_extract(debug_info, '$.sql') sql_list from monitor_debug where access_id = ?";
		String sqlListJson = getJdbcTemplate().queryForObject(sql, String.class, accessId);
		
		JsonNode jsonNode = ObjectMapperCustomer.toJsonNode(sqlListJson);
		List<String> paramList = new ArrayList<String>();
		
		jsonNode.forEach(k -> {
			paramList.add(k.asText());
		});
		
		Map<String, String> sqlMap = new HashMap<String, String>(); 
		if (!paramList.isEmpty()) {
			NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(getJdbcTemplate());
			String md5Sql = "select sql_md5, sql_text from monitor_sql_md5_map where sql_md5 in (:sql_md5)";
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("sql_md5", paramList);
			List<Map<String, Object>> sqlList = template.queryForList(md5Sql, paramMap);
			for (Map<String, Object> m : sqlList) {
				sqlMap.put((String)m.get("sql_md5"), (String)m.get("sql_text"));
			}
		}
				
		for (String md5 : paramList) {
			returnList.add(md5 + ":" + sqlMap.get(md5));
		}
		return returnList;
	}
	


}
