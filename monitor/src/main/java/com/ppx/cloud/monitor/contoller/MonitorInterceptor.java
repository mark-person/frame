package com.ppx.cloud.monitor.contoller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.threads.TaskThread;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.monitor.pojo.AccessLog;
import com.ppx.cloud.monitor.queue.AccessQueue;

/**
 * 监控拦截器
 * @author mark
 * @date 2018年7月2日
 */
public class MonitorInterceptor implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 不监控 /base/... /auth/... /monitor/... 开头的，改成/auto/开头
        
        if (request.getRequestURI().startsWith("/auto/")) {
            AccessLog accessLog = AccessLog.getInstance(request);
            TaskThread.setAccessLog(accessLog);
        }
        else {
            TaskThread.setAccessLog(null);
        }
    	
    	
    	
    	
        return true;
    }
    
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    	AccessLog accessLog = TaskThread.getAccessLog();
    	if (accessLog != null)  {
    	    accessLog.setSpendTime((int)((System.nanoTime() - accessLog.getBeginNanoTime()) / 1e6));
            AccessQueue.offer(accessLog);
    	}
    }
  
}
