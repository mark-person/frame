package com.ppx.cloud.example.solution.price;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ppx.cloud.base.jdbc.MyDaoSupport;

@Service
public class PriceImpl extends MyDaoSupport {

	
	public List<Map<String, Object>> listFreeCart() {
		String sql = "select * from e_free_cart";
		return getJdbcTemplate().queryForList(sql);
	}
	
	public List<Map<String, Object>> calc() {
		int num = 4;
		String countSql = "select sum(sku_price * real_num) total_price from ("
				+ " select e_cart.*, @r := (@minus := sku_num + @minus) - @minus_num n, if(@r <= 0, 0, if(sku_num - @r <= 0, sku_num, @r)) real_num from e_free_cart, "
				+ " (select @minus := 0) t0, (select @minus_num := sum(sku_num) div 4 from e_free_cart) t1 order by sku_price) t";
		
		String subSql = "select e_free_cart.*, @r := (@minus := sku_num + @minus) - @minus_num n, if(@r <= 0, 0, if(sku_num - @r <= 0, sku_num, @r)) real_num from e_free_cart, "
				+ " (select @minus := 0) t0, (select @minus_num := sum(sku_num) div ? from e_free_cart) t1 order by sku_price";
		
		return getJdbcTemplate().queryForList(subSql, num);
		
	}
}
