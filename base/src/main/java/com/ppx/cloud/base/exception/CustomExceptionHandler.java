package com.ppx.cloud.base.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.base.exception.custom.ErrorException;
import com.ppx.cloud.base.exception.custom.FatalException;
import com.ppx.cloud.base.exception.custom.SecuritException;
import com.ppx.cloud.base.mvc.ControllerReturn;



/**
 * 1.Fatal:连接数据库失败，连接池满，内在溢出
 * 2.Error:程序异常，总体还可运行
 * 3.Security:安全异常
 * 4.Warn:警告，可能要处理的, 400?expected 1 actual 0
 * 5.Business:业务异常
 * 
 * 1、2 打印轨迹 
 * 3、4、5 记录一条，不打印轨迹  
 * 
 * 20xxx 返回自己处理
 * 30xxx 业务打印并终止
 * 40xxx 安全或警告打印并终止
 * 50xxx 异常打印并终止
 * 
 * |定义异常处理 分类异常，不是所有的异常都要全部信息，如NoSuchRequestHandlingMethodException
 * MissingServletRequestParameterException 和自定义的异常，不用打印到控制台，提高系统性能
 * 
 * @author mark
 *
 * @date 2018年10月28日
 */
@ControllerAdvice
// 如果使用了监控功能，则会在MonitorExceptionHandler里继承CustomExceptionHandler
@ConditionalOnMissingClass({"com.ppx.cloud.monitor.exception.MonitorExceptionHandler"})
public class CustomExceptionHandler implements HandlerExceptionResolver {

    @ExceptionHandler(value = Throwable.class)
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object object,
            Exception exception) {
    	
    	response.setStatus(500);
    	
    	ExceptionPojo error = ExceptionUtils.getErroCode(exception);
        
        // FatalException和ErrorException的异常需要打印，不需要修改后端代码，不打印
        if (error.getType() == FatalException.class || error.getType() == ErrorException.class) {
           exception.printStackTrace();
        }
        
        // 安全性异常返回403
        if (error.getType() == SecuritException.class) {
            response.setStatus(403);
        }
        

        String accept = request.getHeader("accept");
        if (accept != null && accept.indexOf("text/html") >= 0) {
            ControllerReturn.errorHtml(response, error.getCode(), error.getMsg() + "|" + request.getAttribute("marker"));
        } else {
            ControllerReturn.errorJson(response, error.getCode(), error.getMsg() + "|" + request.getAttribute("marker"));
        }

        return null;
    }

}
