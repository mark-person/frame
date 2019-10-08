package com.ppx.cloud.monitor.output;

import javax.annotation.Resource;
import javax.sql.DataSource;

import com.ppx.cloud.base.jdbc.MyDaoSupport;


/**
 * insert update queryPage等 操作实体对象
 * 
 * @author mark
 * @date 2018年10月28日
 */
public class MonitorDaoSupport extends MyDaoSupport {

	@Resource(name="monitorDataSource")
    public void setDs(DataSource dataSource){
        setDataSource(dataSource);
    }
	
}
