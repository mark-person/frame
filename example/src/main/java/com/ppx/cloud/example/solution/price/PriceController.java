package com.ppx.cloud.example.solution.price;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("base/price")
public class PriceController {
	
	@Autowired
	private PriceImpl priceImpl;
	
	@RequestMapping("/price")
	public ModelAndView price(ModelAndView mv) {
		// 问题(mysql):买4免1,价低者免
		mv.addObject("list", priceImpl.listFreeCart());
		
		mv.addObject("listCalc", priceImpl.calc());
		
		return mv;
	}
	
	
	
}
