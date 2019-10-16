package com.ppx.cloud.example.solution.router;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.base.mobile.MPage;
import com.ppx.cloud.base.mobile.MobileReturn;


@Controller

public class RouterController {
	
	@Autowired
	private RouterImpl impl;
	
	public ModelAndView router(ModelAndView mv) {
		return mv;
	}
	
	
	public Map<String, Object> test(MPage page) {
		
		List<Test> list = impl.list(page);
		return MobileReturn.page(page, list);
	}
	

	

	
	
}
