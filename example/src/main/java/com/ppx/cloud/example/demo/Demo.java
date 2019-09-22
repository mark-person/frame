/**
 * 
 */
package com.ppx.cloud.example.demo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ppx.cloud.base.jdbc.annotation.Id;
import com.ppx.cloud.base.jdbc.annotation.Table;
import com.ppx.cloud.base.util.DateUtils;

/**
 * @author mark
 * @date 2019年7月29日
 */
@Table("core_demo")
public class Demo {
	@Id
	private Integer demoId;
	private String demoName;
	@JsonFormat(pattern = DateUtils.DATE_PATTERN)
	@DateTimeFormat(pattern = DateUtils.DATE_PATTERN)
	private String demoDate;
	private String demoType;
	private Integer demoInt;
	private Float demoNum;
	private Date created;

	public Integer getDemoId() {
		return demoId;
	}

	public void setDemoId(Integer demoId) {
		this.demoId = demoId;
	}

	public String getDemoName() {
		return demoName;
	}

	public void setDemoName(String demoName) {
		this.demoName = demoName;
	}

	public String getDemoDate() {
		return demoDate;
	}

	public void setDemoDate(String demoDate) {
		this.demoDate = demoDate;
	}

	public String getDemoType() {
		return demoType;
	}

	public void setDemoType(String demoType) {
		this.demoType = demoType;
	}

	public Integer getDemoInt() {
		return demoInt;
	}

	public void setDemoInt(Integer demoInt) {
		this.demoInt = demoInt;
	}

	public Float getDemoNum() {
		return demoNum;
	}

	public void setDemoNum(Float demoNum) {
		this.demoNum = demoNum;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

}
