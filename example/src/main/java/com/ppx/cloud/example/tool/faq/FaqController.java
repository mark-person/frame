package com.ppx.cloud.example.tool.faq;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.base.jdbc.page.Page;
import com.ppx.cloud.base.mvc.ControllerReturn;



@Controller
public class FaqController {
	
	@Autowired
	private FaqImpl impl;

	public ModelAndView faq(ModelAndView mv) {
		mv.addObject("data", list(new Page(), new Faq()));	
		return mv;
	}
	
	public Map<String, Object> list(Page page, Faq pojo) {
		
		List<Faq> list = impl.list(page, pojo);
		return ControllerReturn.page(page, list);
	}
	
	public ModelAndView faqAction(ModelAndView mv, Integer id) {
		if (id != null) {
			Faq pojo = impl.get( id);
			mv.addObject("pojo", pojo);
		}
		
		return mv;
	}
	
	public Map<String, Object> insert(Faq pojo, HttpServletRequest request) {
		// 解决, 号问题
		String[] sub = request.getParameterValues("sub");
		if (sub != null && sub.length == 1) {
			pojo.setSub(Arrays.asList(sub[0]));
		}
		String[] answerSub = request.getParameterValues("answerSub");
		if (answerSub != null && answerSub.length == 1) {
			pojo.setAnswerSub(Arrays.asList(answerSub[0]));
		}
		return impl.insert(pojo);
	}
	
	public Map<String, Object> update(Faq pojo, HttpServletRequest request) {
		String[] sub = request.getParameterValues("sub");
		if (sub != null && sub.length == 1) {
			pojo.setSub(Arrays.asList(sub[0]));
		}
		String[] answerSub = request.getParameterValues("answerSub");
		if (answerSub != null && answerSub.length == 1) {
			pojo.setAnswerSub(Arrays.asList(answerSub[0]));
		}
		return impl.update(pojo);
	}

	public Map<String, Object> writeToLocal(Integer id, Boolean force) {
        
	    
	    // String content = "Hello World !!";
	    // Files.write(Paths.get("c:/output.txt"), content.getBytes());
	    
        return impl.writeToLocal(id, force);
    }
}
