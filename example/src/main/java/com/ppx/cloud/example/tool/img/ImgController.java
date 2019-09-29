package com.ppx.cloud.example.tool.img;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("base/img")
public class ImgController {
	
	
	@RequestMapping("/crop")
	public ModelAndView cut(ModelAndView mv, HttpServletRequest request) {
		return mv;
	}
	
	@RequestMapping("/mobile")
	public ModelAndView mobile(ModelAndView mv, HttpServletRequest request) {
		return mv;
	}
	
	@RequestMapping("/cut")
	public ModelAndView mobile(ModelAndView mv) {
		return mv;
	}

}
