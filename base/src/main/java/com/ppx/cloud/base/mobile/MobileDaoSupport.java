package com.ppx.cloud.base.mobile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.util.StringUtils;

import com.ppx.cloud.base.jdbc.MyDaoSupport;



/**
 * insert update queryPage等 操作实体对象
 * 
 * @author mark
 * @date 2018年10月28日
 */
public class MobileDaoSupport extends MyDaoSupport {
	
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	/**
	 * 分页查询对象列表
	 * 
	 * @param c        对象类型
	 * @param page     分页bean
	 * @param cSql     总数sql
	 * @param qSql     查询sql
	 * @param paraList 查询参数
	 * @return 返回对List<对象>
	 */
	protected <T> List<T> queryMPage(Class<T> c, MPage page, StringBuilder cSql, StringBuilder qSql,
			List<Object> paraList) {
		paraList = paraList == null ? new ArrayList<Object>() : paraList;
		int totalRows = super.getJdbcTemplate().queryForObject(cSql.toString(), Integer.class, paraList.toArray());
		page.setTotalRows(totalRows);
		if (totalRows == 0) {
			return new ArrayList<T>();
		}

		// order by
		if (!StringUtils.isEmpty(page.getOrderName())) {
			qSql.append(" order by ").append(page.getOrderName()).append(" ").append(page.getOrderType());
			// 数据库使用快速排序，防止分页的数据出现相同情况
			if (!Objects.equals(page.getOrderName(), page.getUnique()) && !StringUtils.isEmpty(page.getUnique())) {
				qSql.append(",").append(page.getUnique());
			}
		}

		qSql.append(" limit ?, ?");
		paraList.add((page.getPageNumber() - 1) * page.getPageSize());
		paraList.add(page.getPageSize());
		List<T> r = (List<T>) super.getJdbcTemplate().query(qSql.toString(), BeanPropertyRowMapper.newInstance(c),
				paraList.toArray());
		return r;
	}
	
}
