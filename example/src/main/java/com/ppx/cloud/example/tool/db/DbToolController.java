/**
 * 
 */
package com.ppx.cloud.example.tool.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ppx.cloud.base.mvc.ControllerReturn;



/**
 * 
 * @author mark
 * @date 2019年7月18日
 */
@Controller
public class DbToolController {
	
	@Autowired
	private DbToolImpl impl;
	
	public ModelAndView dbTool(ModelAndView mv) {
		List<Map<String, Object>> tableList = impl.listTable();
		mv.addObject("tableList", tableList);
		return mv;
	}
	
	public Map<String, Object> listColumn(String tableName) {
		
		List<Map<String, Object>> list = impl.listColumnMsg(tableName);
		
		List<Map<String, Object>> columnlist = new ArrayList<Map<String, Object>>();
		Map<String, Object> typeMap = new HashMap<String, Object>();
		Map<String, Object> dictMap = new HashMap<String, Object>();
		List<String> singleList = new ArrayList<String>();
		List<String> chainList = new ArrayList<String>();
		
		for (Map<String, Object> map : list) {
			String comment = (String)map.get("comment");
			String value = (String)map.get("value");
			if (comment.contains("|") && comment.contains("{") && comment.contains("}")) {
				String dict = comment.split("\\|")[1];
				List<Map<String, String>> dictList = new ArrayList<Map<String, String>>();
				try {
					@SuppressWarnings("unchecked")
					Map<String, String> jsonDictMap = new ObjectMapper().readValue(dict.getBytes(), Map.class);
					jsonDictMap.keySet().forEach(k -> {
						Map<String, String> itemMap = new HashMap<String, String>();
						itemMap.put("v", k);
						itemMap.put("t", jsonDictMap.get(k));
						dictList.add(itemMap);
					});
				} catch (Exception e) {
					return ControllerReturn.error(30000, value + "字典json错误: " + e.getMessage());
				}
				dictMap.put(value, dictList);
			}
			typeMap.put(value, (String)map.get("DATA_TYPE"));
			
			if (comment.split("select").length == 3) {
				chainList.add(value);
			}
			else if (comment.contains("select")) {
				singleList.add(value);
			}
			
			// 栏位信息
			Map<String, Object> newMap = new HashMap<String, Object>();
			newMap.put("value", value);
			newMap.put("text", (String)map.get("text"));
			newMap.put("key", (String)map.get("COLUMN_KEY"));
			newMap.put("null", (String)map.get("IS_NULLABLE"));
			columnlist.add(newMap);
		}
		return ControllerReturn.of("colList", columnlist, "dict", dictMap, "type", typeMap, "single", singleList, "chain", chainList);
	}
	
	public Map<String, Object> queryData(String tableName, String[] colVal, String qKey, String qOperator, String qValue, DbPage page) {
		List<Map<String, Object>> list = impl.queryData(tableName, colVal, qKey, qOperator, qValue, page);
		return ControllerReturn.of("list", list, "page", page);
	}
	
	// >>>>>>>>>>>>>>>>>>
	public Map<String, Object> listSingleData(String tableName, String columnName, String queryVal) {
		Map<String, Object> map = impl.listSingleData(tableName, columnName, queryVal);
		return ControllerReturn.of(map);
	}
	
	// >>>>>>>>>>>>>>>>>>
	public Map<String, Object> editOrAdd(HttpServletRequest request) {
		Map<String, Object> r = impl.updateOrInsert(request.getParameterMap());
		return r;
	}
	
	// >>>>>>>>>>>>>>>>>>
	public Map<String, Object> delRow(String tableName, String[] idCode, HttpServletRequest request) {
		impl.delRow(tableName, idCode, request.getParameterMap());
		return ControllerReturn.of();
	}
	
	
	// >>>>>>>>>...chain
	public Map<String, Object> listChainData(String tableName, String columnName) {
		List<Map<String, Object>> list = impl.listChainData(tableName, columnName);
		return ControllerReturn.of("list", list);
	}
	
	public Map<String, Object> listChainSecondData(String tableName, String columnName, Integer parentId) {
		List<Map<String, Object>> list = impl.listChainSecondData(tableName, columnName, parentId);
		return ControllerReturn.of("list", list);
	}

}
