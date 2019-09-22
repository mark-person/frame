/**
 * 
 */
package com.ppx.cloud.base.exception;

/**
 * @author mark
 * @date 2019年9月10日
 */
public enum ExceptionCode {
	ERROR_FATAL((byte)9),
	ERROR_UNKNOWN((byte)0), 
	ERROR_JAVA((byte)1), ERROR_THYMELEAF((byte)1), ERROR_DB((byte)3), ERROR_CONFIG((byte)4),
	SECURITY_URL((byte)1), SECURITY_PARAM((byte)2), SECURITY_METHOD((byte)3),
	WARN_URL((byte)1), WARN_PARAM((byte)2);
	
	private byte code;
	
	private ExceptionCode(byte code) {
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
}
