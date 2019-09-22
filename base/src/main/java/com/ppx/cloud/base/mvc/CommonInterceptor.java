package com.ppx.cloud.base.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.base.exception.ExceptionCode;
import com.ppx.cloud.base.exception.ExceptionPojo;
import com.ppx.cloud.base.exception.ExceptionUtils;
import com.ppx.cloud.base.exception.custom.SecuritException;
import com.ppx.cloud.base.exception.custom.WarnException;



/**
 * 拦截器
 * @author mark
 * @date 2019年9月10日
 */
public class CommonInterceptor implements HandlerInterceptor {
	
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
    	String uri = request.getRequestURI();
    	if (uri.length() > 64) {
    		throw new SecuritException(ExceptionCode.SECURITY_URL, "uri length must less than 64");
    	}
    	// 不支持uri带.的请求，权限不好控制且不好统计
        if (uri.indexOf(".") > 0) {
        	throw new SecuritException(ExceptionCode.SECURITY_URL, "uri not supppot .");
        }
        
    	// org.thymeleaf.exceptions.TemplateInputException
    	Exception errorException = (Exception) request.getAttribute("javax.servlet.error.exception");
    	if (errorException != null && errorException.getMessage().indexOf("org.thymeleaf.exceptions") > -1) {
    		ControllerReturn.thymeleafError(response, errorException);
    		return false;
    	}
    	
        // 判断是否为404
        Integer statusCode = (Integer)request.getAttribute("javax.servlet.error.status_code");
        statusCode = statusCode == null ? 200 : statusCode;
        
        if (errorException != null || statusCode != 200) {
            Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
            if (exception == null) {
                exception = new WarnException(ExceptionCode.WARN_URL, "url not found");
            }
            ExceptionPojo c = ExceptionUtils.getErroCode(exception);
            
            
            
            returnResponse(request, response, c.getCode(), c.getMsg());
            return false;
        }
        
        return true;
    }
    
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }
    
    private void returnResponse(HttpServletRequest request, HttpServletResponse response, 
    		int errcode, String errmsg) {
        String accept = request.getHeader("accept");
        if (accept != null && accept.indexOf("text/html") >= 0) {
            ControllerReturn.errorHtml(response, errcode, errmsg);
        } else {
            ControllerReturn.errorJson(response, errcode, errmsg);
        }
    }
}
