package com.ppx.cloud.monitor.output;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.zaxxer.hikari.HikariDataSource;


@Component
public class MonitorDataSourceConfig  {
	@Autowired
	private Environment env;
	
	// @Primary
	@Bean(name="dataSource")
    public DataSource dataSource() {
        
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setJdbcUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        dataSource.setMaximumPoolSize(Integer.parseInt(env.getProperty("spring.datasource.hikari.maximum-pool-size")));
        // dataSource.setConnectionTimeout(Integer.parseInt(env.getProperty("spring.datasource.connection-timeout")));
        // dataSource.setValidationTimeout(Integer.parseInt(env.getProperty("spring.datasource.validation-timeout")));
        // 设置要被执行的SQL语句用来测试连接的有效性
        dataSource.setConnectionTestQuery("select 1");
        
        return dataSource;
    }
	
    /**
     * HikariDataSource默认配置的默认值如下(validate之后)
     * minIdle:10, maxPoolSize:10, maxLifetime:1800000(30分钟), connectionTimeout:30000(30秒),
     * validationTimeout:5000, idleTimeout:600000(10分钟)
     * @return
     */
    @Bean(name="monitorDataSource")
    public DataSource monitorDataSource() {
        
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setJdbcUrl(env.getProperty("monitor.spring.datasource.url"));
        dataSource.setUsername(env.getProperty("monitor.spring.datasource.username"));
        dataSource.setPassword(env.getProperty("monitor.spring.datasource.password"));
        dataSource.setMaximumPoolSize(Integer.parseInt(env.getProperty("monitor.spring.datasource.hikari.maximum-pool-size")));
        // dataSource.setConnectionTimeout(Integer.parseInt(env.getProperty("spring.datasource.connection-timeout")));
        // dataSource.setValidationTimeout(Integer.parseInt(env.getProperty("spring.datasource.validation-timeout")));
        // 设置要被执行的SQL语句用来测试连接的有效性
        dataSource.setConnectionTestQuery("select 1");
        
        return dataSource;
    }
}
