package com.ppx.cloud.auth.console.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.auth.pojo.AuthAccount;
import com.ppx.cloud.auth.pojo.AuthUser;
import com.ppx.cloud.base.jdbc.page.Page;
import com.ppx.cloud.base.mvc.ControllerReturn;


@Controller
public class AuthUserController {

	@Autowired
	private AuthUserImpl impl;
	
    public ModelAndView authUser(ModelAndView mv) {
		mv.addObject("data", list(new Page(), new AuthUser()));
		return mv;
	}
	
	public Map<String, Object> list(Page page, AuthUser pojo) {
		var list = impl.listAuthUser(page, pojo);
		return ControllerReturn.page(page, list);
	}
	
	public Map<?, ?> insert(AuthUser bean) {
		return impl.insert(bean);
	}
	
	public Map<?, ?> getAuthUser(@RequestParam Integer id) {
		return ControllerReturn.of("pojo", impl.getAuthUser(id));
	}
	
	public Map<?, ?> getAccount(@RequestParam Integer id) {
		return ControllerReturn.of("pojo", impl.getAuthAccount(id));
	}
	
	public Map<?, ?> updateName(AuthUser pojo) {
		return impl.updateName(pojo);
	}
	
	public Map<?, ?> updateAccount(AuthAccount pojo) {
		return impl.updateAccount(pojo);
	}
	
	public Map<?, ?> updatePassword(@RequestParam Integer userId, @RequestParam String loginPassword) {
		return impl.updatePassword(userId, loginPassword);
	}
	
	public Map<?, ?> deleteAuthUser(Integer id) {
		return impl.deleteAuthUser(id);
	}
	
    public Map<?, ?> disable(Integer id) {
        return impl.disable(id);
    }
	
    public Map<?, ?> enable(Integer id) {
        return impl.enable(id);
    }

}