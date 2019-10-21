

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







