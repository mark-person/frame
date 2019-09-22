package com.ppx.cloud.auth.login;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ppx.cloud.auth.config.AuthUtils;
import com.ppx.cloud.auth.pojo.AuthAccount;
import com.ppx.cloud.base.exception.ExceptionCode;
import com.ppx.cloud.base.exception.custom.ErrorException;
import com.ppx.cloud.base.exception.custom.SecuritException;
import com.ppx.cloud.base.mvc.ControllerReturn;
import com.ppx.cloud.base.util.CookieUtils;
import com.ppx.cloud.base.util.MD5Utils;

/**
 * 登录
 * 
 * @author mark
 * @date 2018年12月19日
 */
@Controller
public class LoginController {

	@Autowired
	private LoginServiceImpl impl;

	private final static String VALIDATE_TOKEN_PASSWORK = "FSSBBA";

	public ModelAndView login(ModelAndView mv, HttpServletResponse response) throws Exception {
		// 清缓存，保证validateToken最新
		response.setHeader("Content-type", "text/html; charset=utf-8");
		response.setHeader("Pragma","No-cache");
		response.setHeader("Cache-Control","no-cache");
		response.setDateHeader("Expires", 0);
		
		// 
		mv.addObject("v", VALIDATE_TOKEN_PASSWORK);
		return mv;
	}

	public ModelAndView logout(HttpServletResponse response) throws Exception {
		// 清空登录cookie
		CookieUtils.cleanCookie(response, AuthUtils.PPXTOKEN);
		response.sendRedirect("/auth/login/login");
		return null;
	}

	public Map<String, Object> doLogin(HttpServletRequest request, HttpServletResponse response, @RequestParam String a,
			@RequestParam String p, @RequestParam String s) {

		// 验证"验证token",不合法就返回403,预防容易直接请求爆力破解密码
	    String sign = MD5Utils.getMD5(a + p + VALIDATE_TOKEN_PASSWORK);
	    if (!sign.equals(s)) {
	        throw new SecuritException(ExceptionCode.SECURITY_PARAM, "登录签名异常");
	    }
	
		

		AuthAccount account = impl.getLoginAccount(a, p);
		if (account == null) {
			return ControllerReturn.error(30000, "用户名或密码有误");
		} else if (account.getAccountStatus() != AuthUtils.ACCOUNT_STATUS_EFFECTIVE
				|| account.getUserAccountStatus() != AuthUtils.ACCOUNT_STATUS_EFFECTIVE) {
			return ControllerReturn.error(30001, "账号异常");
		} else {
			// 帐号和密码正确，则在cookie上生成一个token, grantAll, grantAuth
			String token = "";
			try {
				// modified用来校验帐号或密码被修改
				Algorithm algorithm = Algorithm.HMAC256(AuthUtils.getJwtPassword());
				token = JWT.create().withIssuedAt(new Date()).withClaim("accountId", account.getAccountId())
						.withClaim("loginAccount", account.getLoginAccount()).withClaim("userId", account.getUserId())
						.withClaim("userName", account.getUserName()).withClaim("modified", account.getModified())
						.sign(algorithm);
				CookieUtils.setCookie(response, AuthUtils.PPXTOKEN, token);

				impl.updateLastLogin(account.getAccountId());
				
				return ControllerReturn.of();
			} catch (Exception e) {
				e.printStackTrace();
				throw new ErrorException(ExceptionCode.ERROR_JAVA, "系统登录异常" + e.getClass().getSimpleName());
			}
		}
	}

}
