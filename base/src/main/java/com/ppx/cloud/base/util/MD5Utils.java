package com.ppx.cloud.base.util;

import org.springframework.util.DigestUtils;


/**
 * 
 * @author mark
 * 2019年9月8日
 */
public class MD5Utils {
    
    public static String getMD5(String str) {
        String md5 = DigestUtils.md5DigestAsHex(str.getBytes());
        return md5;
    }
}
