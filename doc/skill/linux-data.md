
进程还在用lsof
lsof查看删除的文件进程是否还存在

# debugfs
debugfs恢复文件-只对ext2有效

https://blog.51cto.com/13740508/2122407

   主要借助debugfs

    1 运行debugfs，进入调度模式

    2 执行open /dev/sda5  (先df找出设备)

    3 执行ls -d dir 会列出此目录最近的操作，其中可以看到<num>的日志删除记录

    4 执行logdump -i <num> 显示此日志内容

    5 在输出中寻找删除文件对应的block，记录下来blockid

    6退出debugfs，运行dd if=/dev/sda5 of=/tmp/saved  bs=1024 count=1 skip=blockid
此时就把删除的文件恢复了，不过这个方法有个问题，如果删除的是大文件，则占用多个block，操作起来比较麻烦。