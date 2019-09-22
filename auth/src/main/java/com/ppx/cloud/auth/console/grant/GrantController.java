package com.ppx.cloud.auth.console.grant;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.auth.console.res.ResService;
import com.ppx.cloud.auth.pojo.AuthUser;
import com.ppx.cloud.base.jdbc.page.Page;
import com.ppx.cloud.base.mvc.ControllerReturn;


/**
 * 分配权限
 * @author mark
 * @date 2018年7月2日
 */
@Controller
public class GrantController {
	
	@Autowired
	private GrantServiceImpl impl;
	
	@Autowired
	private ResService resourceServ;
	
    public ModelAndView grantToUser() {		
		ModelAndView mv = new ModelAndView();
		mv.addObject("data", listUser(new Page(), new AuthUser()));
		Map<String, Object> resMap = resourceServ.getResource();
		mv.addObject("res", resMap);
		return mv;
	}
	
	public Map<String, Object> listUser(Page page, AuthUser user) {
		List<AuthUser> list = impl.listUser(page, user);
		return ControllerReturn.page(page, list);
	}
	
	public Map<String, Object> getAuthorize(@RequestParam Integer accountId) {
        return ControllerReturn.of("resId", impl.getGrantResIds(accountId));
	}
	
	public Map<String, Object> saveAuthorize(@RequestParam Integer accountId, @RequestParam List<Integer> resId) {
		return impl.saveGrantResIds(accountId, resId);
	}

}