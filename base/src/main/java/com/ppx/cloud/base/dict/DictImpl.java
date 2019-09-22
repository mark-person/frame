package com.ppx.cloud.base.dict;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.util.Strings;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ppx.cloud.base.jdbc.MyDaoSupport;

@Service
public class DictImpl extends MyDaoSupport {
	
//	private static Map<String, String> COLUMN_MAP_TABLE = new ConcurrentHashMap<String, String>();

	public Map<String, Object> list(String[] code) {
	    Map<String, Object> returnMap = new LinkedHashMap<String, Object>();
	    
	    
	    for (String c : code) {
	        if (!c.contains(".")) {
	            returnMap.put("msg", "数据字典code[" + c + "]没有包含.(缺少模块名)");
	            return returnMap;
	        }
	    }
	    
	    boolean useTableNameCache = false;
	    List<String> queryTableName = new ArrayList<String>();
		
//		boolean useTableNameCache = true;
//		List<String> queryTableName = new ArrayList<String>();
//		for (String cName : code) {
//			if (!COLUMN_MAP_TABLE.containsKey(cName)) {
//				useTableNameCache = false;
//				break;
//			}
//			else {
//				queryTableName.add(COLUMN_MAP_TABLE.get(cName));
//			}
//		}
		
		// 通过columnName缓存,  tableName 数据量大时，没tableName速度 
		if (useTableNameCache) {
			NamedParameterJdbcTemplate nameTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("tableNameList", queryTableName);
			paramMap.put("cloumnNameList", Arrays.asList(code));
			String columnTableSql = "select TABLE_NAME, COLUMN_NAME, substring_index(COLUMN_COMMENT, '--', 1) COLUMN_COMMENT from information_schema.columns where table_schema = database()" +
					" and table_name in (:tableNameList) and column_name in (:cloumnNameList)";
			List<Map<String, Object>> list = nameTemplate.queryForList(columnTableSql, paramMap);
			for (Map<String, Object> map : list) {
				setDictJson((String)map.get("COLUMN_NAME"), (String)map.get("COLUMN_COMMENT"), returnMap);
			}
			return returnMap;
		}
		else {
		    String columnSql = "select TABLE_NAME, COLUMN_NAME, substring_index(COLUMN_COMMENT, '--', 1) COLUMN_COMMENT from information_schema.columns where table_schema = database()" +
                    " and table_name like ? and column_name = ?";
		    for (String c : code) {
		        String modelName = c.split("\\.")[0];
		        String cName = c.split("\\.")[1];
		        
		        List<Map<String, Object>> list = getJdbcTemplate().queryForList(columnSql, modelName + "_%", cName);
		        if (list.isEmpty()) {
		            returnMap.put("msg", "数据字典code[" + c + "]不存在");
                    return returnMap;
		        }
		        else if (list.size() > 1) {
		            returnMap.put("msg", "数据字典code[" + c + "]同一模块存在多个同名字典");
                    return returnMap;
		        }
		        else {
		            for (Map<String, Object> map : list) {
		                setDictJson((String)map.get("COLUMN_NAME"), (String)map.get("COLUMN_COMMENT"), returnMap);
                    }
		        }
		    }
		    
		    
//			NamedParameterJdbcTemplate nameTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());
//			Map<String, Object> paramMap = new HashMap<String, Object>();
//			paramMap.put("cloumnNameList", Arrays.asList(code));
//			String columnTableSql = "select TABLE_NAME, COLUMN_NAME, substring_index(COLUMN_COMMENT, '--', 1) COLUMN_COMMENT from information_schema.columns where table_schema = database()" +
//					" and column_name in (:cloumnNameList)";
//			List<Map<String, Object>> list = nameTemplate.queryForList(columnTableSql, paramMap);
//			
//			if (list.size() != code.length) {
//				Set<String> columnNameSet = new HashSet<String>();
//				for (Map<String, Object> map : list) {
//					columnNameSet.add((String)map.get("COLUMN_NAME"));
//				}
//				for (String cName : code) {
//					if (!columnNameSet.contains(cName)) {
//						returnMap.put("msg", "数据字典code[" + cName + "]不存在");
//						return returnMap;
//					}
//				}
//			}
//			
//			for (Map<String, Object> map : list) {
//				COLUMN_MAP_TABLE.put((String)map.get("COLUMN_NAME"), (String)map.get("TABLE_NAME"));
//				setDictJson((String)map.get("COLUMN_NAME"), (String)map.get("COLUMN_COMMENT"), returnMap);
//			}
			return returnMap;
		}
	}
	
	private void setDictJson(String columnName, String comment, Map<String, Object> returnMap) {
		
		if (Strings.isEmpty(comment)) {
			Map<String, String> errorMap = new HashMap<String, String>(1);
			errorMap.put("msg", "数据字典code[" + columnName + "]备注为空");
			returnMap.put(columnName, errorMap);
			return;
		}
		comment = comment.trim();
		String[] item = comment.split("\\|");
		if (item.length < 2) {
			Map<String, String> errorMap = new HashMap<String, String>();
			errorMap.put("msg", "数据字典code[" + columnName + "]缺少 |");
			returnMap.put(columnName, errorMap);
			return;
		}
		
		try {
			JsonNode jn = new ObjectMapper().readTree(item[1]);
			returnMap.put(columnName, jn);
		} catch (IOException e) {
			Map<String, String> errorMap = new HashMap<String, String>(1);
			errorMap.put("msg", "数据字典code[" + columnName + "]json错误:" + e.getMessage());
			returnMap.put(columnName, errorMap);
			return;
		}
		
		// disabled功能
		if (item.length == 3) {
			String disabled = item[2];
			returnMap.put(columnName + "_disabled", disabled);
		}
		
	}
	

}
