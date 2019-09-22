

create table monitor_service (
	service_id			varchar(32) not null,
	service_info 		json,
	service_last_info 	json,
	service_ordinal		tinyint not null default -1,
	service_display		tinyint not null default 1,
	primary key (service_id)
);

create table monitor_startup (
	startup_id		int not null auto_increment,
	service_id		varchar(32) not null,
	startup_time	datetime not null,
	startup_info	json,
	primary key (startup_id)
);

create table monitor_gather (
	service_id 			varchar(32) not null,
	gather_time 		datetime not null,
	is_over				tinyint not null default 0,
	max_processing_time int not null default 0,
	concurrent_num		int not null default 0,
	gather_info			json,
	primary key (service_id, gather_time)
);

create table monitor_stat_sql (
	sql_md5 varchar(32) not null,
	times int not null default 1,
    total_time int not null,
    max_time int not null,
    avg_time int not null,
    max_sql_count int,
    firsted timestamp not null default current_timestamp,
    lasted timestamp not null default current_timestamp,
    distribute json,
    max_detail json,
    uri json,
    primary key (sql_md5)
);

create table monitor_stat_uri (
	uri_seq int not null,
	times int not null default 1,
    total_time int not null,
    max_time int not null,
    avg_time int not null,
    firsted timestamp not null default current_timestamp,
    lasted timestamp not null default current_timestamp,
    distribute json,
    max_detail json,
    primary key (uri_seq)
);

create table monitor_uri_seq_map (
  uri_seq int(11) not null AUTO_INCREMENT,
  uri_text varchar(250) not null,
  primary key (uri_seq),
  unique key idx_monitor_uri_seq_map(uri_text)
);

create table monitor_sql_md5_map (
  sql_md5 varchar(32) not NULL,
  sql_text varchar(1024) default NULL,
  primary key (sql_md5),
  unique key idx_monitor_uri_seq_map (sql_text)
);

create table monitor_access (
	access_id 	int not null auto_increment,
	access_date date not null,
	access_time	time not null,
	service_id 	varchar(32) not null,
	uri_seq		int not null,
	spend_time 	int not null,
	access_info json,
	primary key (access_id, access_date)
) partition by hash (dayofmonth(access_date)) partitions 30;

create table monitor_access_log (
	access_id int not null,
    marker varchar(16),
    log varchar(1024),
    primary key(access_id, marker)
);

create table monitor_debug (
	access_id 	int not null,
	service_id 	varchar(32) not null,
	debug_time	datetime not null,
	debug_info	json,
	primary key (access_id)
);

create table monitor_error (
	access_id 		int not null,
	service_id 		varchar(32) not null,
	error_time 		datetime not null,
	error_code 		int not null default -1,
	error_msg		varchar(1024),
	primary key (access_id)
);

create table monitor_error_detail (
	access_id		int not null,
	error_detail 	json,
	debug_detail 	json,
	primary key (access_id)
);

create table monitor_stat_response (
	service_id 	varchar(32) not NULL,
	hh 			varchar(10) not NULL,
	times 		int	 not null default 1,
    total_time 	int not null,
    max_time	int not null,
    avg_time 	int not null,
    primary key(service_id, hh)
);

create table monitor_stat_warning (
	uri_seq 	int,
	firsted 	timestamp not null default current_timestamp,
    lasted 		timestamp not null default current_timestamp,
    content		int,
    primary key (uri_seq)
);



















/*  @Deprecated  >>>>>>>>>>>>>>>>>>>>>> */
create table access (
	accessId 	int not null auto_increment,
	accessDate 	date not null,
	accessTime	time not null,
	serviceId 	varchar(32) not null,
	uriSeq		int not null,
	spendTime 	int not null,
	accessInfo 		json,
	primary key (accessId, accessDate)
)
partition by hash (dayofmonth(accessDate)) partitions 10;

create table access_log (
	accessId int not null,
    marker varchar(16),
    log varchar(1024),
    primary key(accessId, marker)
);

create table startup (
	startupId		int not null auto_increment,
	startupTime		datetime not null,
	serviceId		varchar(32) not null,
	startupInfo			json,
	primary key (startupId)
);

create table service (
	serviceId		varchar(32) not NULL,
	serviceInfo 	json,
	serviceLastInfo json,
	servicePrio 	tinyint not null default -1,
	serviceDisplay 	tinyint not null default 1,
	primary key (serviceId)
);


create table gather (
	serviceId 			varchar(32) not null,
	gatherTime 			datetime not null,
	isOver				tinyint not null default 0,
	maxProcessingTime 	int not null default 0,
	concurrentN			int not null default 0,
	gatherInfo				json,
	primary key (serviceId, gatherTime)
);

create table error (
	accessId 	int not null,
	serviceId 	varchar(32) not null,
	errorTime 	datetime not null,
	errorCode 	int not null default -1,
	errorLevel  int not null default -1,
	errorMsg	varchar(1024),
	primary key (accessId)
);

create table error_detail (
	accessId 	int not null,
	errorDetail json,
	debugDetail json,
	primary key (accessId)
);

create table debug (
	accessId 	int not null,
	serviceId 	varchar(32) not null,
	debugTime	datetime not null,
	debugInfo		json,
	primary key (accessId)
);


create table map_uri_seq (
  uriSeq int(11) not null AUTO_INCREMENT,
  uriText varchar(250) not null,
  primary key (uriSeq)
);

alter table map_uri_seq add unique index idx_map_uri_text (uriText ASC) VISIBLE;

create table map_sql_md5 (
  sqlMd5 varchar(32) not NULL,
  sqlText varchar(2048) default NULL,
  primary key (sqlMd5)
);

create table stat_uri (
	uriSeq int not null,
	times int not null default 1,
    totalTime int not null default 1,
    maxTime int not null,
    avgTime int not null,
    firsted timestamp not null default current_timestamp,
    lasted timestamp not null default current_timestamp,
    distribute json,
    maxDetail json,
    primary key (uriSeq)
);

create table stat_sql (
	sqlMd5 varchar(32) not null,
	times int not null default 1,
    totalTime int not null default 1,
    maxTime int not null,
    avgTime int not null,
    maxSqlCount int,
    firsted timestamp not null default current_timestamp,
    lasted timestamp not null default current_timestamp,
    distribute json,
    maxDetail json,
    uri json,
    primary key (sqlMd5)
);

create table stat_response (
	serviceId 	 varchar(32) not NULL,
	hh 			 varchar(10) not NULL,
	times int not null default 1,
    totalTime int not null,
    maxTime int not null,
    avgTime int not null,
    primary key(serviceId, hh)
);

create table stat_warning (
	uriSeq int,
	firsted timestamp not null default current_timestamp,
    lasted timestamp not null default current_timestamp,
    content int,
    primary key (uriSeq)
);








