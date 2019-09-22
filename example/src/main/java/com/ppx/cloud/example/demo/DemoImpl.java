package com.ppx.cloud.example.demo;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.ppx.cloud.base.jdbc.MyCriteria;
import com.ppx.cloud.base.jdbc.MyDaoSupport;
import com.ppx.cloud.base.jdbc.page.Page;
import com.ppx.cloud.base.mvc.ControllerReturn;

@Service
public class DemoImpl extends MyDaoSupport {
	
	/**
	 * 1.别乱new
2.别乱if
3.支持分布式
4.防SQL注入
5.防魔法数字
6.防并发
7.防越权
8.能一行代码解决的问题，别写几百行
	 */

	public List<Demo> list(Page page, Demo pojo) {
		
		// 默认排序，后面加上需要从页面传过来的排序的，防止SQL注入
		page.addDefaultOrderName("demo_id").addPermitOrderName("demo_name"); //.addUnique("test_id");

		// 分开两条sql，mysql在count还会执行子查询, 总数返回0将不执行下一句
		
		//  mysql8支持order by和字查询count(*)优化，不支持left join优化
		MyCriteria c = createCriteria("where")
				.addAnd("t.demo_name like ?", "%", pojo.getDemoName(), "%");
		
		 
		//page.addDefaultOrderName("t.test_id").addPermitOrderName("t.demo_name").addUnique("t.demo_id");
		
		StringBuilder cSql = new StringBuilder("select count(*) from core_demo t").append(c);
		StringBuilder qSql = new StringBuilder("select * from core_demo t").append(c);
		
		List<Demo> list = queryForPage(Demo.class, page, cSql, qSql, c.getParaList());
		return list;
	}
	
	public Map<String, Object> insert(Demo pojo) {
        // 后面带不允许重名的字段（该字段需要建索引）
		int r = insertEntity(pojo, "demo_name");
        return ControllerReturn.exists(r, "DEMO_NAME已经存在");
    }
	
	public Demo get(Integer id) {
		String sql = "select demo_id, demo_name, demo_type, demo_int, demo_num, demo_date from core_demo where demo_id = ?";
		Demo pojo = getJdbcTemplate().queryForObject(sql, BeanPropertyRowMapper.newInstance(Demo.class), id);     
        return pojo;
    }
    
    public Map<String, Object> update(Demo pojo) {
        return ControllerReturn.exists(updateEntity(pojo, "demo_name"), "DEMO_NAME已经存在");
    }
    
    public Map<String, Object> delete(Integer id) {
        getJdbcTemplate().update("delete from core_demo where demo_id = ?", id);
        return ControllerReturn.of();
    }

}
