/**
 * 
 */
package com.ppx.cloud.base.mvc;

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


/**
 * 
 * @author mark
 * @date 2019年7月16日
 */
public class ControllerReturn {
	
	private static final String CODE_TITLE = "code";
	
	private static final String MSG_TITLE = "msg";
	
	private static Map<String, Object> SUCCESS = new LinkedHashMap<String, Object>(2);
	static {
		SUCCESS.put(CODE_TITLE, 0);
		SUCCESS.put(MSG_TITLE, "SUCCESS");
	}
	
	private static Map<String, Object> ERROR = new LinkedHashMap<String, Object>(2);
	static {
		ERROR.put(CODE_TITLE, 50000);
		ERROR.put(MSG_TITLE, "ERROR");
	}
	
	public static Map<String, Object> of() {
		return SUCCESS;
	}
	
	public static Map<String, Object> error() {
		return ERROR;
	}
	
	public static Map<String, Object> page(Page page, List<?> list) {
		Map<String, Object> returnMap = new LinkedHashMap<String, Object>(4);
		returnMap.putAll(SUCCESS);
		returnMap.put("page", page);
		returnMap.put("list", list);
		return returnMap;
	}
	
	public static Map<String, Object> page(Page page, List<Object> list, String key, Object value) {
		Map<String, Object> returnMap = new LinkedHashMap<String, Object>(5);
		returnMap.putAll(SUCCESS);
		returnMap.put("page", page);
		returnMap.put("list", list);
		returnMap.put(key, value);
		return returnMap;
	}
	
	
	// 20XXX业务逻辑自己处理  
	public static Map<String, Object> of(int code, String msg) {
		if (code >= 20000 && code <= 29999) {
			Map<String, Object> returnMap = new LinkedHashMap<String, Object>(2);
			returnMap.put(CODE_TITLE, code);
			returnMap.put(MSG_TITLE, msg);
			return returnMap;
		}
		else {
			throw new RuntimeException("code must be from 20000-29999, code:" + code);
		}
	}
	
	// 30XXX业务异常，系统处理
	public static Map<String, Object> error(int code, String msg) {
		if (code >= 30000 && code <= 30999) {
			Map<String, Object> returnMap = new LinkedHashMap<String, Object>(2);
			returnMap.put(CODE_TITLE, code);
			returnMap.put(MSG_TITLE, msg);
			return returnMap;
		}
		else {
			throw new RuntimeException("code must be from 30000-30999, code:" + code);
		}
	}
	
	public static Map<String, Object> exists(int result, String msg) {
        if (result == 0) {
            Map<String, Object> returnMap = new LinkedHashMap<String, Object>(2);
            returnMap.put(CODE_TITLE, "30000");
            returnMap.put(MSG_TITLE, msg);
            return returnMap;
        }
        else {
            return SUCCESS;
        }
    }
	
	public static Map<String, Object> of(Map<String, Object> map) {
		Map<String, Object> returnMap = new LinkedHashMap<String, Object>();
		returnMap.putAll(SUCCESS);
		returnMap.putAll(map);
		return returnMap;
	}
	
	public static Map<String, Object> of(String key, Object value) {
		Map<String, Object> returnMap = new LinkedHashMap<String, Object>(3);
		returnMap.putAll(SUCCESS);
		returnMap.put(key, value);
		return returnMap;
	}
	
	public static Map<String, Object> of(String k1, Object v1, String k2, Object v2) {
		Map<String, Object> returnMap = new LinkedHashMap<String, Object>(4);
		returnMap.putAll(SUCCESS);
		returnMap.put(k1, v1);
		returnMap.put(k2, v2);
		return returnMap;
	}
	
	public static Map<String, Object> of(String k1, Object v1, String k2, Object v2, String k3, Object v3) {
		Map<String, Object> returnMap = new LinkedHashMap<String, Object>(5);
		returnMap.putAll(SUCCESS);
		returnMap.put(k1, v1);
		returnMap.put(k2, v2);
		returnMap.put(k3, v3);
		return returnMap;
	}
	
	public static Map<String, Object> of(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4) {
		Map<String, Object> returnMap = new LinkedHashMap<String, Object>(6);
		returnMap.putAll(SUCCESS);
		returnMap.put(k1, v1);
		returnMap.put(k2, v2);
		returnMap.put(k3, v3);
		returnMap.put(k4, v4);
		return returnMap;
	}
	
	public static Map<String, Object> of(String k1, Object v1, String k2, Object v2, String k3, Object v3, 
			String k4, Object v4, String k5, Object v5) {
		Map<String, Object> returnMap = new LinkedHashMap<String, Object>(8);
		returnMap.putAll(SUCCESS);
		returnMap.put(k1, v1);
		returnMap.put(k2, v2);
		returnMap.put(k3, v3);
		returnMap.put(k4, v4);
		returnMap.put(k5, v5);
		return returnMap;
	}
	

    public static boolean errorJson(HttpServletResponse response, int code, String msg) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        Map<String, Object> returnMap = new LinkedHashMap<String, Object>(2);
        returnMap.put(CODE_TITLE, code);
        returnMap.put(MSG_TITLE, msg);
        try (PrintWriter printWriter = response.getWriter()) {
            String returnJson = new ObjectMapper().writeValueAsString(returnMap);
            printWriter.write(returnJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static void errorHtml(HttpServletResponse response, int code, String msg) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.write("System busy[" + code + "]" + msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void thymeleafError(HttpServletResponse response, Exception errorException) {
        try (PrintWriter printWriter = response.getWriter()) {
            // TODO 改成一个方法，并显示系统忙和代码
            ExceptionPojo c = ExceptionUtils.getErroCode(errorException);
            printWriter.write("<script>onload=function(){document.write('System busy[" +  c.getCode() + "]" + c.getMsg() + "');}</script>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
  
    
}
