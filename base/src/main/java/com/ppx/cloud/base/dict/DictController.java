package com.ppx.cloud.base.dict;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;



@Controller
public class DictController {
	
	@Autowired
	private DictImpl impl;
	
	
	public ModelAndView list(HttpServletResponse response, @RequestParam String[] code) throws Exception {
		
		response.setHeader("content-type", "application/javascript;charset=utf-8");
		// 单位秒
		response.setHeader("cache-control", "max-age=600");
		
		Map<String, Object> map = impl.list(code);
		String msg = (String)map.get("msg");
		if (Strings.isNotEmpty(msg)) {
		    try (PrintWriter pw = response.getWriter()) {
	            pw.write("alert('" + msg + "');");
	            response.flushBuffer();
	        }
		}
		else {
		    try (PrintWriter pw = response.getWriter()){
	            String json = new ObjectMapper().writeValueAsString(map);
	            pw.write("var _dictTmp = " + json + ";if (typeof dict != 'undefined') {for (i in _dictTmp) {dict[i] = _dictTmp[i]}} else {var dict = _dictTmp};");
	            response.flushBuffer();
	        }
		}
		
		
		return null;
	}
	 
	

}
