/**
 * 
 */
package com.ppx.cloud.base.config.pojo;

import java.util.Date;

/**
 * @author mark
 * @date 2019年1月17日
 */
public class ConfigResult {
	private String configName;
	private String serviceId;
	private Integer execResult;
	private String execDesc;
	private Date modified;
	
	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getExecResult() {
		return execResult;
	}

	public void setExecResult(Integer execResult) {
		this.execResult = execResult;
	}

	public String getExecDesc() {
		return execDesc;
	}

	public void setExecDesc(String execDesc) {
		this.execDesc = execDesc;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	

}
