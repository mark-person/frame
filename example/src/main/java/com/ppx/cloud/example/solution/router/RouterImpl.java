package com.ppx.cloud.example.solution.router;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ppx.cloud.base.mobile.MPage;
import com.ppx.cloud.base.mobile.MobileDaoSupport;

@Service
public class RouterImpl extends MobileDaoSupport {


	public List<Test> list(MPage page) {
		
		var c = createCriteria("where").addAnd("t.test_name = ?", null);
		
		var cSql = new StringBuilder("select count(*) from test t").append(c);
		var qSql = new StringBuilder("select * from test t").append(c);
		List<Test> list = queryMPage(Test.class, page, cSql, qSql, c.getParaList());
		
		
		
		return list;
	}
}
