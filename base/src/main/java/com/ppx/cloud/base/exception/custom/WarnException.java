package com.ppx.cloud.base.exception.custom;

import com.ppx.cloud.base.exception.CustomException;
import com.ppx.cloud.base.exception.ExceptionCode;

/**
 * 
 * @author mark
 * 2019年9月8日
 */
@SuppressWarnings("serial")
public class WarnException extends CustomException {
	
	public WarnException(ExceptionCode suffixCode, String msg) {
    	super(suffixCode, msg);
    }
}


