package com.ppx.cloud.example.demo;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.auth.annotation.ActionAuth;
import com.ppx.cloud.base.jdbc.page.Page;
import com.ppx.cloud.base.mvc.ControllerReturn;


@ActionAuth
@Controller
public class DemoController {
	
	@Autowired
	private DemoImpl impl;
	
	public ModelAndView demo(ModelAndView mv, HttpServletRequest request) {
		// mv.setViewName("demo/demo/demo");
		
		mv.addObject("data", list(new Page(), new Demo()));
		
		// mv.addObject("insertOrUpdate");
		
		
		return mv;
	}
	
	public Map<String, Object> list(Page page, Demo pojo) {
//		try {
//			Thread.sleep(2000);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		
		
		List<Demo> list = impl.list(page, pojo);
		return ControllerReturn.page(page, list);
	}
	 
    public Map<String, Object> insert(Demo pojo) {
		

		
        return impl.insert(pojo);
    }
    
    public Map<String, Object> update(Demo pojo) {
//      try {
//      Thread.sleep(1000);
//  } catch (Exception e) {
//      // TODO: handle exception
//  }
        
        return impl.update(pojo);
    }
    
    public Map<String, Object> get(@RequestParam Integer id) {
        return ControllerReturn.of("pojo", impl.get(id));
    }

    public Map<String, Object> delete(@RequestParam Integer id) {
        return impl.delete(id);
    }

    
    public Map<String, Object> test() {
        return ControllerReturn.of("list", impl.test());
    }
}
