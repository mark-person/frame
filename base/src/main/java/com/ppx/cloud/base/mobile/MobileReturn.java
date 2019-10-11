/**
 * 
 */
package com.ppx.cloud.base.mobile;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ppx.cloud.base.exception.ExceptionPojo;
import com.ppx.cloud.base.exception.ExceptionUtils;
import com.ppx.cloud.base.jdbc.page.Page;
import com.ppx.cloud.base.mvc.ControllerReturn;


/**
 * 
 * @author mark
 * @date 2019年7月16日
 */
public class MobileReturn extends ControllerReturn {
	
	
	public static Map<String, Object> page(MPage page, List<?> list) {
		Map<String, Object> returnMap = new LinkedHashMap<String, Object>(4);
		returnMap.putAll(SUCCESS);
		returnMap.put("page", page);
		returnMap.put("list", list);
		return returnMap;
	}
	
	public static Map<String, Object> page(MPage page, List<Object> list, String key, Object value) {
		Map<String, Object> returnMap = new LinkedHashMap<String, Object>(5);
		returnMap.putAll(SUCCESS);
		returnMap.put("page", page);
		returnMap.put("list", list);
		returnMap.put(key, value);
		return returnMap;
	}
	
	
    
  
    
}
