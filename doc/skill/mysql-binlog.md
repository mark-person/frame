

mysqlbinlog --help
mysqlbinlog: [ERROR] unknown variable 'default-character-set=utf8mb4'

  两个方法可以解决这个问题

一是在MySQL的配置/etc/my.cnf中将default-character-set=utf8 修改为 character-set-server = utf8，但是这需要重启MySQL服务，如果你的MySQL服务正在忙，那这样的代价会比较大。
二是用mysqlbinlog --no-defaults mysql-bin.000004 命令打开

使用 -d 选项，可以指定一个数据库名称(或者:--database)


mysqlbinlog --no-defaults “D:\Program Files\MySQL\mysql-8.0.12-winx64\data\binlog.000079” > binlog.txt


* 查看特定开始时间的条目 $ mysqlbinlog --start-datetime="2017-08-16 10:00:00" mysqld-bin.000001
* 查看特定结束时间的条目 $ mysqlbinlog --stop-datetime="2017-08-16 15:00:00" mysqld-bin.000001

* 跳过前N个条目 $ mysqlbinlog -o 10000 mysqld-bin.000001
* mysqlbinlog输出调试信息 $ mysqlbinlog --debug-check mysqld-bin.000001
* 


# show processlist 
### show full processlist
* echo 'show processlist;'|mysql --socket=/tmp/mysql3306.sock > test.dd



您也可以使用mysqladmin processlist语句得到此信息。如果您有SUPER权限，您可以看到所有线程。s

mysqladmin -u root -p processlist
Enter password:


mysqladmin -uroot -pdadong123 processlist       #<==查看执行的SQL语句信息。
mysqladmin -uroot -pdadong123 processlist -i 1  #<==每秒查看一次执行的SQL语句。

processlist

Show a list of active server threads. This is like the output of the SHOW PROCESSLIST statement. If the --verbose option is given, the output is like that of SHOW FULL PROCESSLIST. (See Section 13.7.7.29, “SHOW PROCESSLIST Syntax”.)

--verbose, -v

Verbose mode. Print more information about what the program does.


SELECT a.trx_id, a.trx_state, a.trx_started, a.trx_query, b.ID, b.USER, b.DB, b.COMMAND, b.TIME, b.STATE, b.INFO, c.PROCESSLIST_USER, c.PROCESSLIST_HOST, c.PROCESSLIST_DB, d.SQL_TEXT FROM information_schema.INNODB_TRX a LEFT JOIN information_schema.PROCESSLIST b ON a.trx_mysql_thread_id = b.id AND b.COMMAND = 'Sleep' LEFT JOIN PERFORMANCE_SCHEMA.threads c ON b.id = c.PROCESSLIST_ID LEFT JOIN PERFORMANCE_SCHEMA.events_statements_current d ON d.THREAD_ID = c.THREAD_ID;


show full processlist

SELECT * FROM INFORMATION_SCHEMA.INNODB_TRX




