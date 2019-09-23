package com.ppx.cloud.example.tool.img;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class ImgController {
	
	
	public ModelAndView cut
	(ModelAndView mv, HttpServletRequest request) {
	
		System.out.println(".....out:123");
		
		return mv;
	}
	

}
