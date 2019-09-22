package com.ppx.cloud.base.exception;


import java.util.HashMap;
import java.util.Map;

import org.attoparser.ParseException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.util.NestedServletException;

import com.ppx.cloud.base.exception.custom.ErrorException;
import com.ppx.cloud.base.exception.custom.FatalException;
import com.ppx.cloud.base.exception.custom.SecuritException;
import com.ppx.cloud.base.exception.custom.UnknownException;
import com.ppx.cloud.base.exception.custom.WarnException;

/**
 * 分类异常，不是所有的异常都要全部信息
 * 0:不需要修改后端代码，不打印
 * 9:紧急异常，需要紧急处理，打印
 * 1:java.lang, jdbc, thymeleaf等异常，  局部异常
 * @author mark
 * @date 2018年12月26日
 */



public class ExceptionUtils {
	
    private static Map<Class<?>, Class<? extends CustomException>> exceptionMap = new HashMap<Class<?>, Class<? extends CustomException>>();
    private static Map<Class<?>, ExceptionCode> exceptionTypeMap = new HashMap<Class<?>, ExceptionCode>();
 
    
    static {
     
    	// WarnException >>>>>>>>>>>>>>>>
    	
    	// GET访问POST
    	exceptionMap.put(HttpRequestMethodNotSupportedException.class, SecuritException.class);
    	exceptionTypeMap.put(HttpRequestMethodNotSupportedException.class, ExceptionCode.SECURITY_METHOD);
    	
    	// 参数转换错误
    	exceptionMap.put(MethodArgumentTypeMismatchException.class, WarnException.class);
    	exceptionTypeMap.put(MethodArgumentTypeMismatchException.class, ExceptionCode.WARN_PARAM);
        // 找到uri，参数缺少    
    	exceptionMap.put(MissingServletRequestParameterException.class, WarnException.class);
    	exceptionTypeMap.put(MissingServletRequestParameterException.class, ExceptionCode.WARN_PARAM);
        // 找到uri和参数，参数类型不匹配
    	exceptionMap.put(TypeMismatchException.class, WarnException.class);
    	exceptionTypeMap.put(TypeMismatchException.class, ExceptionCode.WARN_PARAM);
        // 单条记录查询结果为空,防攻击时打印大量错误日志
        // Spring中使用JdbcTemplate的queryForObject方法，当查不到数据时会抛出如下异常：EmptyResultDataAccessException
    	exceptionMap.put(EmptyResultDataAccessException.class, WarnException.class);
    	exceptionTypeMap.put(EmptyResultDataAccessException.class, ExceptionCode.WARN_PARAM);
    	// 需要接收json参数 如：@RequestBody Test test
    	exceptionMap.put(HttpMessageNotReadableException.class, WarnException.class);
    	exceptionTypeMap.put(HttpMessageNotReadableException.class, ExceptionCode.WARN_PARAM);
    	
    	
    	exceptionMap.put(ParseException.class, ErrorException.class);
        exceptionTypeMap.put(ParseException.class, ExceptionCode.ERROR_THYMELEAF);
    	
    	
        
    	// FatalException >>>>>>>>>>>>>>
        // 数据库连接不上(如没有启动，或死掉)
    	exceptionMap.put(CannotGetJdbcConnectionException.class, FatalException.class);
    	exceptionTypeMap.put(CannotGetJdbcConnectionException.class, ExceptionCode.ERROR_FATAL);
        // 数据库连接池已满
    	exceptionMap.put(CannotCreateTransactionException.class, FatalException.class);
    	exceptionTypeMap.put(CannotCreateTransactionException.class, ExceptionCode.ERROR_FATAL);
    	
    	
    }
    
	public static ExceptionPojo getErroCode(Throwable e) {
	    
	    
	    
	    String msg = e.getClass().getSimpleName() + ":" + e.getMessage();
	    
	    if ("com.ppx.cloud.base.exception.custom".equals(e.getClass().getPackage().getName())) {
	    	CustomException customException = (CustomException)e;
	    	return new ExceptionPojo(customException.getSuffixCode(), msg, customException.getClass());
	    }
	    
	    
	    if (exceptionMap.containsKey(e.getClass())) {
	    	return new ExceptionPojo(exceptionTypeMap.get(e.getClass()), msg, exceptionMap.get(e.getClass()));
	    }
		
		if (e instanceof BindException) {
			// 参数类型错误或 查询每页记录超过最大数时
			int i = e.getMessage().indexOf("IllegalRequestException");
			msg = (i >= 0) ? "maxPageSize forbidden" : msg;
			return new ExceptionPojo(ExceptionCode.SECURITY_PARAM, msg, SecuritException.class);
		}
		else if (e instanceof NestedServletException) {
			if (msg.indexOf("OutOfMemoryError") >= 0) {
				return new ExceptionPojo(ExceptionCode.ERROR_JAVA, msg, FatalException.class);
			}
			if (msg.indexOf("NoClassDefFoundError") >= 0) {
			    return new ExceptionPojo(ExceptionCode.ERROR_JAVA, msg, ErrorException.class);
			}
			if (msg.indexOf("org.thymeleaf.exceptions") >= 0) {
			    return new ExceptionPojo(ExceptionCode.ERROR_THYMELEAF, msg, ErrorException.class);
			}
			return new ExceptionPojo(ExceptionCode.ERROR_JAVA, msg, ErrorException.class);
		}
		
		
		if ("java.lang".equals(e.getClass().getPackage().getName())) {
			// java.lang异常 NullPointerException等
		    return new ExceptionPojo(ExceptionCode.ERROR_JAVA, msg, ErrorException.class);
		}
		else if ("org.springframework.jdbc".equals(e.getClass().getPackage().getName())) {
			// jdbc异常，表不存，sql语法错误
		    return new ExceptionPojo(ExceptionCode.ERROR_DB, msg, ErrorException.class);
		}
		else if ("org.springframework.dao".equals(e.getClass().getPackage().getName())) {
			// 必须栏插入空值，重复数据，Integer field返回int
		    return new ExceptionPojo(ExceptionCode.ERROR_DB, msg, ErrorException.class);
		}
		else if ("org.thymeleaf.exceptions".equals(e.getClass().getPackage().getName())) {
		    return new ExceptionPojo(ExceptionCode.ERROR_THYMELEAF, msg, ErrorException.class);
		}
		return new ExceptionPojo(ExceptionCode.ERROR_UNKNOWN, msg, UnknownException.class);
	}
}
