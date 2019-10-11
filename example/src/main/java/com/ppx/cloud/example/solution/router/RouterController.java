package com.ppx.cloud.example.solution.router;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("base/router")
public class RouterController {
	
	@RequestMapping("/router")
	public ModelAndView router(ModelAndView mv) {
		
		return mv;
	}
	
	@RequestMapping("/home")
	public ModelAndView home(ModelAndView mv) {
		
		return mv;
	}
	
	

	

	
	
}
