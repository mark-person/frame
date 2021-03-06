package com.ppx.cloud.auth.common;


/**
 * 分配权限上下文
 * @author mark
 * @date 2018年12月21日
 */
public class AuthContext {
	
	public static ThreadLocal<LoginAccount> threadLocalAccount = new ThreadLocal<LoginAccount>();

	public static void setLoginAccount(LoginAccount mer) {
	    threadLocalAccount.set(mer);
	}
	
//	public static LoginAccount getLoginAccount() {
//		LoginAccount a = new LoginAccount();
//		a.setAccountId(-1);
//		a.setUserId(-1);
//		return a;
//	}
	
	public static LoginAccount getLoginAccount() {
        return threadLocalAccount.get();
    }
}
