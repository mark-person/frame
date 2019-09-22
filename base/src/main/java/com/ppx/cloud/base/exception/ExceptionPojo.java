package com.ppx.cloud.base.exception;

import org.apache.logging.log4j.util.Strings;

import com.ppx.cloud.base.exception.custom.BusinessException;
import com.ppx.cloud.base.exception.custom.ErrorException;
import com.ppx.cloud.base.exception.custom.FatalException;
import com.ppx.cloud.base.exception.custom.SecuritException;
import com.ppx.cloud.base.exception.custom.WarnException;

public class ExceptionPojo {
    
    private int code;
    private String msg;
    private Class<? extends CustomException> type;
    
    // 
    public ExceptionPojo(ExceptionCode suffixCode, String msg, Class<? extends CustomException> type) {
    	
    	int prefix = 0;
    	if (type == BusinessException.class) {
    		prefix = 30000;
    	}
    	else if (type == WarnException.class || type == SecuritException.class) {
    		prefix = 40000;
    	}
    	else if (type == ErrorException.class || type == FatalException.class) {
    		prefix = 50000;
    	}
    	
        this.code = prefix + suffixCode.getCode();
        this.msg = msg;
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        if (Strings.isNotEmpty(msg) && msg.length() > 1024) {
            msg = msg.substring(0, 1024);
        }
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<? extends CustomException> type) {
        this.type = type;
    }

}
