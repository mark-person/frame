package com.ppx.cloud.auth.console.res;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.ppx.cloud.base.mvc.ControllerReturn;
import com.ppx.cloud.base.util.ApplicationUtils;



@Controller
public class ResController {
	
	@Autowired
	private ResServiceImpl impl;

		
    public ModelAndView res(ModelAndView mv) {
		mv.addObject("res", getResource());
		return mv;
	}
	
	public Map<String, Object> getResource() {
		Map<String, Object> resMap = impl.getResource();
		if (resMap == null) {
			return ControllerReturn.of(30000, "资源为空");
		}
		return ControllerReturn.of("tree", resMap);
	}
	
	public Map<?, ?> getResUri() {
		List<String> menuList = new ArrayList<String>();
		
        RequestMappingHandlerMapping r = ApplicationUtils.context.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = r.getHandlerMethods();
        
        List<String> resList = new ArrayList<String>();
        resList.add("*");
        Set<String> controllerSet = new HashSet<String>();
        
        Set<RequestMappingInfo> set =  map.keySet();
        for (RequestMappingInfo info : set) {
            Set<String> uriSet = info.getPatternsCondition().getPatterns(); 
            for (String uri : uriSet) {  
                if (!filterUri(uri)) {
                	if (uri.startsWith("/auto/") && isMenu(info) ) {
                		menuList.add(uri.replaceFirst("/auto/", ""));
                	}
                	if (uri.startsWith("/auto/")) {
                    	String[] u = uri.split("/");
                        controllerSet.add(u[2] + "/*");
                        resList.add(uri.replaceFirst("/auto/", ""));
                    }
                }
            }
        }
        resList.addAll(controllerSet);
        
//        try {
//            Thread.sleep(5000);
//        } catch (Exception e) {
//            // TODO: handle exception
//        }
        
        return ControllerReturn.of("resList", resList, "menuList", menuList);
    }
	
	private boolean filterUri(String uri) {
		// 排除监控部分 /error
        if (uri.startsWith("/error")) return true;
        
        return false;
	}
	
	private boolean isMenu(RequestMappingInfo info) {
		Set<RequestMethod> methodSet = info.getMethodsCondition().getMethods();
    	if (methodSet.contains(RequestMethod.GET)) {
    		return true;
    	}
    	return false;
	}
	
    // >>>>>>>>>>>>>>>....new
    public Map<?, ?> insert(@RequestParam int parentId, @RequestParam String resName,
    		@RequestParam int resType, String menuUri, String menuParam) {
    	if (Strings.isNotBlank(menuParam)) {
    		menuUri = menuUri + "?" + menuParam;
    	}
        impl.insert(parentId, resName, resType, menuUri);
        return getResource();
    }
    
    public Map<String, Object> update(@RequestParam int id, @RequestParam String resName, String menuUri, String menuParam) {
    	if (Strings.isNotBlank(menuParam)) {
    		menuUri = menuUri + "?" + menuParam;
    	}
    	 impl.update(id, resName, menuUri);
    	 return getResource();
    }
    
    public Map<?, ?> deleteRes(@RequestParam int id) {
    	return impl.deleteRes(id);
    }
    
    public Map<?, ?> updateResPrio(String[] id) {
    	impl.updateResPrio(id);
    	return getResource();
    }
    
    public Map<?, ?> insertUri(@RequestParam Integer resId, String[] uri) {
    	impl.insertUri(resId, uri);
		return ControllerReturn.of();
	}	
    
    public Map<String, Object> getUri(@RequestParam Integer resId,  @RequestParam Integer resType) {
		List<String> list = impl.getUri(resId);
		String menuUri = "";
		if (resType == 1) {
		    menuUri = impl.getMenuUri(resId);
		}
		
		return ControllerReturn.of("list", list, "menuUri", menuUri);
				
	}
    
    public Map<String, Object> getMenuUri(@RequestParam int resId) {
        String menuUri = impl.getMenuUri(resId);
        return ControllerReturn.of("menuUri", menuUri);
	}
    
    
    
    
    
    
    
    
    
    
}