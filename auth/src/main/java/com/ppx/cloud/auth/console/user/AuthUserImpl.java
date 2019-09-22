package com.ppx.cloud.auth.console.user;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ppx.cloud.auth.config.AuthUtils;
import com.ppx.cloud.auth.pojo.AuthAccount;
import com.ppx.cloud.auth.pojo.AuthUser;
import com.ppx.cloud.base.jdbc.MyCriteria;
import com.ppx.cloud.base.jdbc.MyDaoSupport;
import com.ppx.cloud.base.jdbc.page.Page;
import com.ppx.cloud.base.mvc.ControllerReturn;


/**
 * 
 * @author mark
 * @date 2018年12月16日
 */
@Service
public class AuthUserImpl extends MyDaoSupport {

	
	public List<AuthUser> listAuthUser(Page page, AuthUser u) {
		MyCriteria c = createCriteria("and")
				.addAnd("u.user_name like ?", "%", u.getUserName(), "%");
		
		var whereSql = "from auth_user u left join auth_account a on u.user_id = a.account_id where u.user_status >= ?";
		var cSql = new StringBuilder("select count(*) ").append(whereSql).append(c);
		var qSql = new StringBuilder("select u.*, a.login_account, a.account_status ")
				.append(whereSql).append(c).append(" order by u.user_id");
		c.addPrePara(AuthUtils.ACCOUNT_STATUS_EFFECTIVE);
		var list = queryForPage(AuthUser.class, page, cSql, qSql, c.getParaList());

		
		return list;
	}

	@Transactional
	public Map<String, Object> insert(AuthUser pojo) {
		AuthAccount account = new AuthAccount();
		account.setUserId(-1);
		account.setLoginAccount(pojo.getLoginAccount());
		String pw = AuthUtils.getMD5Password(pojo.getLoginPassword());
		account.setLoginPassword(pw);
		int accountR = insertEntity(account, "login_account");
		if (accountR == 0) {
			return ControllerReturn.exists(accountR, "登录账号已经存在");
		}
		
		int userId = getLastInsertId();
		pojo.setUserId(userId);
		int userR = insertEntity(pojo, "user_name");
		if (userR == 0) {
			// 名称导致保存失败，刚删除上面插入的账号
		    String delSql = "delete from auth_account where account_id = ?";
		    getJdbcTemplate().update(delSql, userId);
            return ControllerReturn.exists(userR, "名称已经存在");
		}
		
		getJdbcTemplate().update("update auth_account set user_id = ? where account_id = ?", userId, userId);
		return ControllerReturn.of();
	}

	public AuthUser getAuthUser(Integer id) {
		var pojo = getJdbcTemplate().queryForObject("select * from auth_user where user_id = ?",
				BeanPropertyRowMapper.newInstance(AuthUser.class), id);
		return pojo;
	}
	
	public AuthAccount getAuthAccount(Integer id) {
		var pojo = getJdbcTemplate().queryForObject("select * from auth_account where account_id = ?",
				BeanPropertyRowMapper.newInstance(AuthAccount.class), id);
		return pojo;
	}

	public Map<String, Object> updateName(AuthUser pojo) {
		int r = updateEntity(pojo, "user_name");
		return ControllerReturn.exists(r, "用户名称已经存在");
	}
	
	public Map<String, Object> updateAccount(AuthAccount pojo) {
		pojo.setModified(new Date());
		int r = updateEntity(pojo, "login_account");
		return ControllerReturn.exists(r, "登录帐号已经存在");
	}
	
	public Map<String, Object> updatePassword(Integer userId, String loginPassword) {
		String sql = "update auth_account set login_password = ?, modified = now() where account_id = ?";
		getJdbcTemplate().update(sql, AuthUtils.getMD5Password(loginPassword), userId);
		return ControllerReturn.of();
	}

	@Transactional
	public Map<String, Object> deleteAuthUser(Integer userId) {
    	getJdbcTemplate().update("update auth_user set user_status = ? where user_id = ?", 0, userId);
    	getJdbcTemplate().update("update auth_account set account_status = ? where account_id = ?", 0, userId);
    	return ControllerReturn.of();
	}
	
	public Map<String, Object> disable(Integer userId) {
	    getJdbcTemplate().update("update auth_user set user_status = ? where user_id = ?"
	            , AuthUtils.ACCOUNT_STATUS_INEFFECTIVE, userId);
	    getJdbcTemplate().update("update auth_account set account_status = ? where account_id = ?"
	            , AuthUtils.ACCOUNT_STATUS_INEFFECTIVE, userId);
	    return ControllerReturn.of("accountStatus", AuthUtils.ACCOUNT_STATUS_INEFFECTIVE);
    }
	
	public Map<String, Object> enable(Integer userId) {
        getJdbcTemplate().update("update auth_user set user_status = ? where user_id = ?"
                , AuthUtils.ACCOUNT_STATUS_EFFECTIVE, userId);
        getJdbcTemplate().update("update auth_account set account_status = ? where account_id = ?"
                , AuthUtils.ACCOUNT_STATUS_EFFECTIVE, userId);
        return ControllerReturn.of("accountStatus", AuthUtils.ACCOUNT_STATUS_EFFECTIVE);
    }
	
}