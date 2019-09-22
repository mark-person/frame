


# maven打包，由父类打包，在父类maven install

# 去掉vue的警告
preferences 搜索 validation 属性用*


# 去掉多余的搜索项
* 1.eclipse maven项目查询排除target目录

```
frame项目 -> 右键，properties-->Resource Filters
1.Exclude all
2.Folders + All children(recursive)
3.target
```

* 2.Open Resource的Filter Duplicated Resources


# 监控 否则cpu 物理内存等不能用
--add-opens jdk.management/com.sun.management.internal=ALL-UNNAMED




# eclipse 设置
```
# 打包
在父类中打包

# tab设置成4个空格
打开eclipse-windows-preferences 
General -> Editors -> Text Editors -> 钩上Insert spaces for tabs -> Apply and Close
Java -> Code Style -> Formatter -> New... xxx -> Tab policy 选择Spaces only

#  基本设置(UTF-8, Unix)
Preferences -> General -> Workspace:Text file encodeing:UTF-8, New text file line delimiter:Unix

# properties乱码
Preferences -> General -> Content Types:Java Properties File:Default encoding:UTF-8

# pom.xml的execution标签错误
菜单:Window->Perferences->Maven->Lifecycle Mapping插入内容
<?xml version="1.0" encoding="UTF-8"?>
<lifecycleMappingMetadata>
    <pluginExecutions>   
        <pluginExecution>
            <pluginExecutionFilter>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-dependency-plugin</artifactId>
                <goals>
                    <goal>unpack</goal>
                </goals>
                <versionRange>[1.3,)</versionRange>
            </pluginExecutionFilter>
            <action>
                <ignore />
            </action>
        </pluginExecution>        
    </pluginExecutions>
</lifecycleMappingMetadata>

# 去掉 WARNING: An illegal reflective access operation has occurred
${jrebel_args} --add-opens java.base/java.lang=ALL-UNNAMED

# 去掉WARNING: Illegal reflective access by com.google.protobuf.UnsafeUtil
-XX:+IgnoreUnrecognizedVMOptions --add-opens=java.base/java.nio=ALL-UNNAMED


${jrebel_args} --add-opens java.base/java.lang=ALL-UNNAMED -XX:+IgnoreUnrecognizedVMOptions --add-opens=java.base/java.nio=ALL-UNNAMED --add-opens jdk.management/com.sun.management.internal=ALL-UNNAMED
```


# 技巧

Run--External Tools--External Tools Configurations...
Name：C:/WINDOWS/explorer.exe
Arguments：${container_loc}

Ctrl+Shitf+L 二次 run 找到 Run last External 按F10

* 作者设置eclipse
-Duser.name=mark

# 监控 否则cpu部分不能用
--add-opens jdk.management/com.sun.management.internal=ALL-UNNAMED

