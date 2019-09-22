package com.ppx.cloud.monitor.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.base.jdbc.page.Page;
import com.ppx.cloud.base.mvc.ControllerReturn;
import com.ppx.cloud.base.util.ApplicationUtils;


@Controller
public class ConsoleController {
	
	@Autowired
	private ConsoleImpl impl;
	
	private final String TITLE = "监控平台V0.1.0";
	
	
    public ModelAndView console(ModelAndView mv) {
		mv.addObject("listService", impl.listIndexService());	
		mv.addObject("currentServiceId", ApplicationUtils.getServiceId());
		mv.addObject("title", TITLE);
		return mv;
	}
	
    public ModelAndView service() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("data", listAllService());
		return mv;
	}
	
	public Map<String, Object> listAllService() {	
		List<Map<String, Object>> list = impl.listAllService();
	    return ControllerReturn.of("list", list);
	}
	
	public Map<String, Object> display(@RequestParam String serviceId, @RequestParam int display) {
		impl.display(serviceId, display);	
		return ControllerReturn.of("display", display);
	}
	
	public Map<String, Object> orderService(@RequestParam List<String> serviceId) {
		impl.orderService(serviceId);		
		return listAllService();
	}
	
	public ModelAndView startup(ModelAndView mv) {
		mv.addObject("data", listStartup(new Page(), null));
		mv.addObject("listService", impl.listDisplayService());
		return mv;
	}
	public Map<String, Object> listStartup(Page page, String serviceId) {
		List<Map<String, Object>> list = impl.listStartup(page, serviceId);
		return ControllerReturn.page(page, list);
	}
	
	public ModelAndView access() {
		ModelAndView mv = new ModelAndView();
		String today = today();
		mv.addObject("today", today);
		mv.addObject("data", listAccess(new Page(), today, null, null, null, null));
		mv.addObject("listService", impl.listDisplayService());
		return mv;
	}
	
	public Map<String, Object> listAccess(Page page, String date, String beginTime, String endTime, String serviceId, String uriText) {
		List<Map<String, Object>> list = impl.listAccess(page, date, beginTime, endTime, serviceId, uriText);
		return ControllerReturn.page(page, list);
	}
	
	public Map<String, Object> getAccess(String accessId) {
		return impl.getAccess(accessId);
	}
	
	public ModelAndView error() {
		ModelAndView mv = new ModelAndView();
		String today = today();
		mv.addObject("today", today);
		mv.addObject("data", listError(new Page(), today, null, null, null));
		mv.addObject("listService", impl.listDisplayService());
		return mv;
	}
	public Map<String, Object> listError(Page page, String date, String beginTime, String endTime, String serviceId) {
		List<Map<String, Object>> list = impl.listError(page, date, beginTime, endTime, serviceId);
		return ControllerReturn.page(page, list);
	}
	public Map<String, Object> getError(String accessId) {
	    return ControllerReturn.of();
	}
	
	public ModelAndView gather() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("data", listGather(new Page(), null));
		mv.addObject("listService", impl.listDisplayService());
		return mv;
	}
	public Map<String, Object> listGather(Page page, String serviceId) {
		List<Map<String, Object>> list = impl.listGather(page, serviceId);
		return ControllerReturn.page(page, list);
	}
	
	
	public ModelAndView statUri(ModelAndView mv) {
		mv.addObject("data", listStatUri(new Page(), null));
		
		return mv;
	}
	public Map<String, Object> listStatUri(Page page, String uri) {
		List<Map<String, Object>> list = impl.listStatUri(page, uri);
		return ControllerReturn.page(page, list);
	}

	public ModelAndView statSql(ModelAndView mv) {
		mv.addObject("data", listStatSql(new Page(), null));
		return mv;
	}
	public Map<String, Object> listStatSql(Page page, String sql) {
		List<Map<String, Object>> list = impl.listStatSql(page, sql);
		return ControllerReturn.page(page, list);
	}
	
	public ModelAndView statResponse(ModelAndView mv) {
		mv.addObject("data", listStatResponse(new Page(), null));
		mv.addObject("listService", impl.listDisplayService());
		return mv;
	}
	public Map<String, Object> listStatResponse(Page page, String serviceId) {
		List<Map<String, Object>> list = impl.listStatResponse(page, serviceId);
		return ControllerReturn.page(page, list);
	}
	
	public ModelAndView statWarning(ModelAndView mv) {
		mv.addObject("data", listStatWarning(new Page(), null));
		return mv;
	}
	public Map<String, Object> listStatWarning(Page page, String uriText) {
		 List<Map<String, Object>> list = impl.listStatWarning(page, uriText);
		return ControllerReturn.page(page, list);
	}
	
	public ModelAndView debug(ModelAndView mv) {
		mv.addObject("listService", impl.listDisplayService());
		mv.addObject("data", listDebug(new Page(), null, null, null, null, null, null));
		return mv;
	}
	
	public Map<String, Object> listDebug(Page page, String serviceId, String date, String beginTime, String endTime,
			String uri, String marker) {
		List<Map<String, Object>> list = impl.listDebug(page, serviceId, date, beginTime, endTime, uri, marker);
		return ControllerReturn.page(page, list);
	}
	
	public Map<String, Object> listDebugSql(String accessId) {
		List<String> list = impl.listDebugSql(accessId);
	    return ControllerReturn.of("list", list);
	}
	
		
	
	private String today() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}

	
	
}