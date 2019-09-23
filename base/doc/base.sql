
create table base_config_service (
	service_id 		varchar(32) not null,
	artifact_id		varchar(32) not null,
	service_status	tinyint not null default 1 comment '服务状态|{"0":"无效","1":"有效 "}',
	service_desc	varchar(32),
	primary key (service_id)
);

create table base_config_value (
	config_name 		varchar(64) not null comment '名称|每个名称对应一个ConfigExec的实现类',
	artifact_id			varchar(32) not null,
	config_type			varchar(8) not null comment '配置类型|{"method":"方法刷新","arg":"参数刷新"}',
	config_status		varchar(8) not null default 'init' comment '配置状态|{"init":"初始","all":"全部刷新","part":"部分刷新"}',
	config_value		json comment 'JSON值 -- [{"key":"KEY", "value":"VALUE"}]',
	config_desc			varchar(64),
	modified			timestamp not null default current_timestamp,
	primary key (config_name)
);





















INSERT INTO `base_config_value` (`config_name`,`artifact_id`,`config_type`,`config_value`,`config_desc`) 
	VALUES ('AUTH_CONFIG','frame-example','arg','{\"JWT_PASSWORD\": \"JWTPASSWORD\", \"ADMIN_PASSWORD\": \"ee70e58531bc525d5b818be90357faa4\", \"JWT_VALIDATE_SECOND\": 86400}','明文:admin');
INSERT INTO `base_config_value` (`config_name`,`artifact_id`,`config_type`,`config_value`,`config_desc`) 
	VALUES ('AUTH_ALL','frame-example','method',NULL,'清除所有权限缓存(不需初始化)');
INSERT INTO `base_config_value` (`config_name`,`artifact_id`,`config_type`,`config_value`,`config_desc`) 
	VALUES ('AUTH_GRANT','frame-example','method',NULL,'清除分配权限缓存(不需初始化)');
INSERT INTO `base_config_value` (`config_name`,`artifact_id`,`config_type`,`config_value`,`config_desc`) 
	VALUES ('MONITOR_SWITCH','frame-example','arg','{\"IS_DEBUG\": true, \"IS_WARNING\": true}',NULL);
INSERT INTO `base_config_value` (`config_name`,`artifact_id`,`config_type`,`config_value`,`config_desc`) 
	VALUES ('MONITOR_THRESHOLD','frame-example','arg','{\"MAX_CPU_DUMP\": 0.9, \"DUMP_MAX_TIME\": 5000, \"GATHER_INTERVAL\": 300000, \"MAX_MEMORY_DUMP\": 0.9}',NULL);

create table base_config_result (
	config_name		varchar(64) not null,
	service_id 		varchar(32) not null,
	exec_result 	tinyint not null default 1 comment '配置执行结果|{"0":"失败","1":"成功"}',
 	exec_desc 	 	varchar(256),
 	modified		timestamp not null default current_timestamp,
	primary key (config_name, service_id)
);









