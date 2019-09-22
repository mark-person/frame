package com.ppx.cloud.auth.console.child;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.auth.console.grant.GrantServiceImpl;
import com.ppx.cloud.auth.console.res.ResServiceImpl;
import com.ppx.cloud.auth.pojo.AuthAccount;
import com.ppx.cloud.base.jdbc.page.Page;
import com.ppx.cloud.base.mvc.ControllerReturn;

/**
 * 子帐号管理
 * @author mark
 * @date 2018年7月2日
 */
@Controller
public class AuthChildController {

    @Autowired
    private AuthChildImpl impl;

    public ModelAndView authChild(ModelAndView mv) {
        mv.addObject("data", list(new Page(), new AuthAccount()));
        return mv;
    }
  
    public Map<String, Object> list(Page page, AuthAccount child) {
        List<AuthAccount> list = impl.listChild(page, child);
        return ControllerReturn.page(page, list);
    }
    
    public Map<String, Object> insert(AuthAccount bean) {
        return impl.insert(bean);
    }
    
    public Map<?, ?> getChild(@RequestParam Integer id) {
        return ControllerReturn.of("pojo", impl.getChild(id));
    }

    public Map<?, ?> updateAccount(AuthAccount bean) {
        return impl.updateAccount(bean);
    }
   
    public Map<?, ?> updatePassword(@RequestParam Integer accountId,
            @RequestParam String loginPassword) {
        return impl.updatePassword(accountId, loginPassword);
    }

    public Map<?, ?> deleteChild(Integer id) {
        return impl.deleteChild(id);
    }
    
    public Map<?, ?> disable(Integer id) {
    	return impl.disable(id);
    }
    
    public Map<?, ?> enable(Integer id) {
    	return impl.enable(id);
    }
    
    
    

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>子帐号权限
    @Autowired
    private GrantServiceImpl grantImpl;

    @Autowired
    private ResServiceImpl resourceImpl;

    public ModelAndView grantToChild() {
        ModelAndView mv = new ModelAndView();
        mv.addObject("data", listChildAccount(new Page(), new AuthAccount()));
        
        Map<String, Object> resMap = resourceImpl.getResource();
        mv.addObject("res", resMap);
        return mv;
    }

    public Map<String, Object> listChildAccount(Page page, AuthAccount child) {
        List<AuthAccount> list = impl.listChild(page, child);
        return ControllerReturn.page(page, list);
    }

    public Map<?, ?> getAuthorize(@RequestParam Integer accountId) {
        return ControllerReturn.of("resId", grantImpl.getGrantResIds(accountId));
    }

    public Map<?, ?> saveAuthorize(@RequestParam Integer accountId, @RequestParam List<Integer> resId) {
        return grantImpl.saveGrantResIds(accountId, resId);
    }
    

}