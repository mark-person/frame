package com.ppx.cloud.auth.index;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.auth.common.AuthContext;
import com.ppx.cloud.auth.common.LoginAccount;


/**
 * 首页,菜单，修改密码
 * @author mark
 * @date 2018年12月18日
 */
@Controller
public class IndexController {
    
    @Autowired
    private PasswordServiceImpl passwrodImpl;
    
    public ModelAndView home(ModelAndView mv) {
        return mv;
    }  
    
    public ModelAndView adminHome(ModelAndView mv) {
        return mv;
    }
	
    public ModelAndView editPassword(ModelAndView mv) {
		return mv;
	}	
	
	public Map<?, ?> updatePassword(@RequestParam String oldP, @RequestParam String newP) {
		return passwrodImpl.updatePassword(oldP, newP);
	}
	
	
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>菜单>>>>>>>>>>>>>>>>>>>>>>
	@Autowired
	private MenuServiceImpl menuService;
	
	public ModelAndView menu(ModelAndView mv) {	
		LoginAccount account = AuthContext.getLoginAccount();
		mv.addObject("account", account);
		if (account.isAdmin()) {			
			mv.addObject("menu", getAdminResource());
		}
		else {
		    List<Map<String, Object>> menu = menuService.getMenu();
		    if (menu == null) {
		        menu = new ArrayList<Map<String, Object>>();
		    }
		    if (account.isMainAccount()) {
		        menu.addAll(getMainAcountResource());
		    }
			mv.addObject("menu", menu);
		}
		return mv;
	}
	
	private List<Map<String, Object>> getMainAcountResource() {
        // 超级管理员的固定菜单
        List<Map<String, Object>> dirList = new ArrayList<Map<String, Object>>();           
        List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();
        
        // 菜单项1
        Map<String, Object> menuMap = new LinkedHashMap<String, Object>();
        menuMap.put("t", "子帐号管理");
        menuMap.put("id", -1);
        menuMap.put("s", 0);
        menuMap.put("uri", "/auth/authChild/authChild");
        menuList.add(menuMap);
        
        menuMap = new LinkedHashMap<String, Object>();
        menuMap.put("t", "子帐号权限");
        menuMap.put("id", -2);
        menuMap.put("s", 0);
        menuMap.put("uri", "/auth/authChild/grantToChild");
        menuList.add(menuMap);
        
        // 目录项0
        Map<String, Object> systemMap = new LinkedHashMap<String, Object>();
        systemMap.put("t", "子帐号管理");
        systemMap.put("id", -3);
        systemMap.put("s", 0);
        systemMap.put("c", menuList);
        
        dirList.add(systemMap);
        return dirList;
    }
	
	private List<Map<String, Object>> getAdminResource() {
		// 超级管理员的固定菜单
		List<Map<String, Object>> dirList = new ArrayList<Map<String, Object>>();			
		List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();
		
		// 菜单项1
		Map<String, Object> menuMap = new LinkedHashMap<String, Object>();
		menuMap.put("t", "资源管理");
		menuMap.put("id", 0);
		menuMap.put("s", 0);
		menuMap.put("uri", "/auth/res/res");
		menuList.add(menuMap);
		
		menuMap = new LinkedHashMap<String, Object>();
		menuMap.put("t", "用户管理");
		menuMap.put("id", 1);
		menuMap.put("s", 0);
		menuMap.put("uri", "/auth/authUser/authUser");
		menuList.add(menuMap);
		
		menuMap = new LinkedHashMap<String, Object>();
		menuMap.put("t", "权限管理");
		menuMap.put("id", 2);
		menuMap.put("s", 0);
		menuMap.put("uri", "/auth/grant/grantToUser");
		menuList.add(menuMap);
		
		menuMap = new LinkedHashMap<String, Object>();
        menuMap.put("t", "配置管理");
        menuMap.put("id", 3);
        menuMap.put("s", 0);
        menuMap.put("uri", "/base/config/config");
        menuList.add(menuMap);
		
		
		// 目录项0
		Map<String, Object> systemMap = new LinkedHashMap<String, Object>();
		systemMap.put("t", "系统管理");
		systemMap.put("id", -1);
		systemMap.put("s", 1);
		systemMap.put("c", menuList);
		
		dirList.add(systemMap);
		return dirList;
	}
	
}