package com.ppx.cloud.auth.console.child;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ppx.cloud.auth.common.AuthContext;
import com.ppx.cloud.auth.config.AuthUtils;
import com.ppx.cloud.auth.pojo.AuthAccount;
import com.ppx.cloud.base.jdbc.MyCriteria;
import com.ppx.cloud.base.jdbc.MyDaoSupport;
import com.ppx.cloud.base.jdbc.page.LimitRecord;
import com.ppx.cloud.base.jdbc.page.Page;
import com.ppx.cloud.base.mvc.ControllerReturn;

/**
 * 子帐号管理
 * @author mark
 * @date 2018年7月2日
 */
@Service
public class AuthChildImpl extends MyDaoSupport {

	public List<AuthAccount> listChild(Page page, AuthAccount child) {
		int userId = AuthContext.getLoginAccount().getUserId();
		
		
		MyCriteria c = createCriteria("and")
				.addAnd("account_id = ?", child.getAccountId())
				.addAnd("login_account like ?", "%", child.getLoginAccount(), "%");
		
		String sql = "from auth_account where user_id = ? and account_status >= ? and user_id != account_id";
		StringBuilder cSql = new StringBuilder("select count(*) ").append(sql).append(c);
		StringBuilder qSql = new StringBuilder("select account_id, login_account, account_status ").append(sql).append(c).append(" order by account_id desc");
		c.addPrePara(userId);
		c.addPrePara(AuthUtils.ACCOUNT_STATUS_EFFECTIVE);
		List<AuthAccount> list = queryForPage(AuthAccount.class, page, cSql, qSql, c.getParaList());

		return list;
	}

	@Transactional
	public Map<String, Object> insert(AuthAccount bean) {
		int userId = AuthContext.getLoginAccount().getUserId();
		
		AuthAccount account = new AuthAccount();
		account.setUserId(userId);
		account.setLoginAccount(bean.getLoginAccount());
		account.setLoginPassword(AuthUtils.getMD5Password(bean.getLoginPassword()));
		int r = insertEntity(account, "login_account");
		return ControllerReturn.exists(r, "账号已经存在");
	}

	public AuthAccount getChild(Integer id) {
		AuthAccount bean = getJdbcTemplate().queryForObject("select * from auth_account where account_id = ?",
				BeanPropertyRowMapper.newInstance(AuthAccount.class), id);
		return bean;
	}
	
	public Map<?, ?> updateAccount(AuthAccount bean) {
		int userId = AuthContext.getLoginAccount().getUserId();
		bean.setModified(new Date());
		// 帐号唯一，只能更新自己子帐号的
		int r = updateEntity(bean, LimitRecord.newInstance("user_id", userId), "login_account");
		return ControllerReturn.exists(r, "登录账号已经存在");
	}
	
	public Map<?, ?> updatePassword(Integer id, String loginPassword) {
		int merId = AuthContext.getLoginAccount().getUserId();
		// 只能更新自己子帐号的
		String sql = "update auth_account set login_password = ?, modified = now() where account_id = ? and user_id = ?";
		int r = getJdbcTemplate().update(sql, AuthUtils.getMD5Password(loginPassword), id, merId);
		return ControllerReturn.of("val", r);
	}

	public Map<?, ?> deleteChild(Integer id) {
		int userId = AuthContext.getLoginAccount().getUserId();
		// 只能删除自己子帐号
		int r = getJdbcTemplate().update("update auth_account set account_status = ? where account_id = ? and user_id = ?", 0, id , userId);
		return ControllerReturn.of("val", r);
	}
	
	public Map<?, ?> disable(Integer id) {
        int userId = AuthContext.getLoginAccount().getUserId();
        // 只能disable自己子帐号
        getJdbcTemplate().update("update auth_account set account_status = ? where account_id = ? and user_id = ?"
                , AuthUtils.ACCOUNT_STATUS_INEFFECTIVE, id , userId);
        return ControllerReturn.of("accountStatus", AuthUtils.ACCOUNT_STATUS_INEFFECTIVE);
    }
	
	public Map<?, ?> enable(Integer id) {
        int userId = AuthContext.getLoginAccount().getUserId();
        // 只能enable自己子帐号
        getJdbcTemplate().update("update auth_account set account_status = ? where account_id = ? and user_id = ?"
                , AuthUtils.ACCOUNT_STATUS_EFFECTIVE, id , userId);
        return ControllerReturn.of("accountStatus", AuthUtils.ACCOUNT_STATUS_EFFECTIVE);
    }

}





