# Jar Analyzer Cli
![](https://img.shields.io/badge/build-passing-brightgreen)
![](https://img.shields.io/badge/build-Java%208-orange)
![](https://img.shields.io/github/downloads/4ra1n/jar-analyzer-cli/total)
![](https://img.shields.io/github/v/release/4ra1n/jar-analyzer-cli)

[English Version](doc/README.md)

## 介绍

该项目是 [Jar Analyzer](https://github.com/4ra1n/jar-analyzer) 的命令行版本，更容易上手，更好的自定义分析与搜索

该项目可以把一个或一堆`jar`文件变成一个`sqlite`数据库，然后自行编写`sql`语句分析

## Quick Start

构建`rt.jar`数据库（耗时一分钟以内）

```shell
java -jar jar-analyzer-cli.jar build --jar "/path/to/rt.jar"
```

构建`weblogic`数据库（耗时几分钟）

```shell
java -jar jar-analyzer-cli.jar build --jar "/path/to/Oracle/wlserver/modules"
```

数据库的表有
- anno_table: 注解表
- class_file_table: class文件位置表
- class_table: 类信息表
- interface_table: 接口表
- jar_table: jar文件表
- member_table: 类成员变量表
- method_call_table: 方法调用表
- method_impl_table: 方法实现表
- method_table: 方法信息表

## Thanks

<img src="https://resources.jetbrains.com/storage/products/company/brand/logos/IntelliJ_IDEA.svg" alt="IntelliJ IDEA logo.">


This project is developed using JetBrains IDEA.
Thanks to JetBrains for providing me with a free license, which is a strong support for me.