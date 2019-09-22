package com.ppx.cloud.base.exception.custom;

import com.ppx.cloud.base.exception.CustomException;
import com.ppx.cloud.base.exception.ExceptionCode;

/**
 * 
 * @author mark
 * 2019年9月8日
 */
@SuppressWarnings("serial")
public class ErrorException extends CustomException {
	
	public ErrorException(ExceptionCode suffixCode, String msg) {
    	super(suffixCode, msg);
    }
}


