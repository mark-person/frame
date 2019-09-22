package com.ppx.cloud.base.exception;


/**
 * 
 * @author mark
 * @date 2019年9月9日
 */
@SuppressWarnings("serial")
public class CustomException extends RuntimeException {
	
	private ExceptionCode suffixCode;
	
	public CustomException(ExceptionCode suffixCode, String msg) {
		super(msg);
		this.suffixCode = suffixCode;
    }

	public ExceptionCode getSuffixCode() {
		return suffixCode;
	}

	public void setSuffixCode(ExceptionCode suffixCode) {
		this.suffixCode = suffixCode;
	}
	
	
}

