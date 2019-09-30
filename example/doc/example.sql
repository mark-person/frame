

/** 买4免1,价低者免 */
create table e_free_cart (
	sku_id int not null comment 'SKU_ID',
	sku_num int not null comment '数量',
	sku_price decimal(7, 2) not null comment 'PRICE',
	primary key (sku_id)
) comment '';




/** 活动设计 */

create table e_marketing_activity (
	activity_id			int not null auto_increment,
	activity_name		varchar(64) not null,
	activity_begin		date not null,
	activity_end		date not null,
	activity_status		varchar(5) not null,
	primary key(activity_id)
) comment '营销活动';

create table e_marketing_program (
	program_id			int not null auto_increment,
	activity_id			int not null,
	program_type 		json not null,
	program_name		varchar(64) not null,
	program_ordinal		tinyint not null,
	primary key(program_id)
) comment '营销方案';

/**
 * 10元均价 10元3件 加1元多1件 买4免1 满10元立减3元
 */
create table e_marketing_program_type (
	program_type 		json not null,
	program_type_name 	varchar(64) not null,
	primary key(program_type)
) comment '营销方案类型';

create table e_program_target (
	program_id		int not null,
	target_id		int not null,
	primary key(program_id, target_id)
)

/**
 * >>>>>>>>>>>>>>>>>>>>>>
 * ->活动启动后生成冗余数据，过期和作废的清除
 * 日期处理，生成日期对应的program_id
 */
create table e_program_target_active (
	program_id			int not null,
	target_id			int not null,
	program_type 		json not null,
	program_ordinal		tinyint not null,
	primary key(program_id, target_id)
) comment '方案';

create table e_program_target_data (
	activity_date	date not null comment '活动日期-- 活动启动时生成,9.1~9.3生成3个',
	program_id		int not null,
	primary key(activity_date, program_id)
) comment '方案日期 -- 活动日期决定';

create table e_target (
	target_id		int not null auto_increment,
	target_type 	tinyint not null comment '目标类型 -- 类目、专题、品牌、群组、商品、SKU',
	primary key(target_id)
) comment '目标';

create table e_target_sku (
	target_id	int not null,
	sku_id		int not null,
	primary key(target_id, sku_id)
) comment '目标对应SKU';



/* 
需求：找出购物车每个SKU的营销策略(买4免1等)
解决群组问题：类目、专题、品牌、群组、商品、SKU对应的营销策略(买4免1等)
解决日期问题：从2019.8.26~2019.8.27
解决冲突问题: 冲突取营销策略优化级最高的
*/
select s.sku_id, substring_index(group_concat(a.program_type order by a.program_ordinal desc) , ',', 1) program_type
from 
(
select 1 sku_id
union 
select 3 sku_id) cart
left join e_target_sku s on cart.sku_id = s.sku_id
left join e_program_target_active a on a.target_id = s.target_id 
	and program_id in (select program_id from e_program_target_data where activity_date = '2019-08-26')
group by s.sku_id


/**
用json存select JSON_EXTRACT('{"Y":10}', '$.Y')

P:10(10元均价)
N:3,Y:10(10元3件)
A:1(加一元多一件)
B:4(买4免1)
D:0.5(5折)
D:0.9,2:0.8(单件9折，第二件8折)
D:0.9,2+:0.8(单件9折，第二件及以上8折)
1:0.9,2:0.8(第一件9折，任两件8折)
1:0.9,2+:0.8(第一件9折，任两件及以上8折)
E:10,-3(满10元立减3元)

* 价低者免，价低者折

特价
-- 10元均价 10元3件 加1元多1件 买4免1 满10元立减3元
select JSON_EXTRACT('{"P":10}', '$.P');
select JSON_EXTRACT('{"Y":10,"N":3}', '$.Y');
select JSON_EXTRACT('{"A":1}', '$.A');
select JSON_EXTRACT('{"B":4}', '$.B');
select JSON_EXTRACT('{"E":10, "M":3}', '$.E');
-- 5折 单件9折，第1件8折  单件9折，第1件及以上8折  第1件9折，任2件8折  第1件9折，任2件及以上8折
select JSON_EXTRACT('{"D":0.5}', '$.D');
select JSON_EXTRACT('{"D1":0.9,"D2":0.8}', '$.D2');
select JSON_EXTRACT('{"D1":0.9,"D2M":0.8}', '$.D2M');
select JSON_EXTRACT('{"D1":0.9,"D2A":0.8"}', '$.D2A');
select JSON_EXTRACT('{"D1":0.9,"D2MA":0.8"}', '$.D2MA');
 */





















create table core_demo_main (
	main_id 	int not null auto_increment comment '主ID',
	main_name 	varchar(32) not null comment '主名称',
	primary key (main_id)
) comment '主表';

create table core_demo_sub (
	 sub_id 		int not null auto_increment comment 'SUB_ID',
	 main_id		int not null comment '主名;select main_id, main_name from core_demo_main',
	 sub_name		varchar(32) not null comment 'SUB名称',
	 primary key (sub_id)
) comment '从表';

create table core_more (
	main_id		int not null comment '主ID;select main_id, main_name from core_demo_main',
	sub_id		int not null comment 'SUB_ID;select sub_id, sub_name from core_demo_sub',
	more_type	tinyint not null comment '类型|{"MAIN":"主","SUB":"从"}',
	primary key(main_id, sub_id)
) comment '多对多';

create table core_demo (
  demo_id 		int not null auto_increment comment 'ID--其它说明',
  sub_id		int comment '规则名字|select sub_id, sub_name from core_demo_sub where main_id = ?|select main_id, main_name from core_demo_main',
  demo_name 	varchar(32) not null comment '标题',
  demo_date 	date comment '日期',
  demo_type		varchar(5) comment '类型|{"NEW":"新的","OLD":"旧的"}',
  demo_int		int comment '数量',
  demo_num	 	decimal(7,2) comment '金额',
  created		timestamp not null default current_timestamp,
  primary key (demo_id)
) comment '样例';
/** 如果是invisible，这样优化器就会忽略这个索引，但是索引依然存在于引擎内部 */
ALTER TABLE core_demo ADD INDEX idx_demo_name (demo_name ASC) VISIBLE;




/** faq */
create table core_faq (
	faq_id 			int not null auto_increment,
    faq_title 		varchar(64) not null, 
    faq_category	varchar(5) not null,
    faq_time_type	varchar(5) not null,
    faq_created		timestamp not null default current_timestamp,
    faq_descr		text,
    faq_answer		text,
    primary key(faq_id)
);

create table core_faq_descr_item (
	faq_id			 int not null,
	faq_descr_index  int not null,
    item_content	 text not null,
    primary key(faq_id, faq_descr_index)
);

create table core_faq_answer_item (
    faq_id			 int not null,
	faq_answer_index int not null,
    item_content	 text not null,
    primary key(faq_id, faq_answer_index)
);


