/**
 * 
 */
package com.ppx.cloud.example.tool.db;

/**
 * @author mark
 * @date 2019年8月15日
 */
public class DbPage {
	private Integer limit;
	private Integer count;

	private String orderName;
	private String orderType;

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		if (limit == null || limit >= 200) {
			this.limit = 50;
		}
		this.limit = limit;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

}
