package com.ppx.cloud.example.solution.router;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.base.mobile.MPage;
import com.ppx.cloud.base.mobile.MobileReturn;


@Controller
@RequestMapping("base/router")
public class RouterController {
	
	@Autowired
	private RouterImpl impl;
	
	@RequestMapping("/router")
	public ModelAndView router(ModelAndView mv) {
		return mv;
	}
	
	@RequestMapping("/home")
	public ModelAndView home(ModelAndView mv) {
		return mv;
	}
	
	@RequestMapping("/test")
	public Map<String, Object> test(MPage page) {
		
		
		List<Test> list = impl.list(page);
		return MobileReturn.page(page, list);
	}
	

	

	
	
}
