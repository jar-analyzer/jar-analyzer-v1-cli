# Jar Analyzer Cli
![](https://img.shields.io/badge/build-passing-brightgreen)
![](https://img.shields.io/badge/build-Java%208-orange)
![](https://img.shields.io/github/downloads/4ra1n/jar-analyzer-cli/total)
![](https://img.shields.io/github/v/release/4ra1n/jar-analyzer-cli)

[English Version](doc/README.md)

## 介绍

该项目是 [Jar Analyzer](https://github.com/4ra1n/jar-analyzer) 的命令行版本，更容易上手，更好的自定义分析与搜索

运行的原理：
- 根据输入的 `jar` 文件或者 `jars` 目录，自动分析所有的 `class` 文件内容，并构建数据库
- 内置 `jython` 解释器以及常用搜索脚本，简单的命令即可通过构建的数据库进行搜索
- 如果内置脚本不满足你的需求，可以自行编写脚本通过 `jython` 解释器执行查询

相比 `Jar Analyzer GUI` 版将所有分析结果保存在内存 `HashMap` 中，命令行版将分析数据保存在 `sqlite` 数据库中会更有意义，例如可以做到**一次构建多次分析**的效果，或者自行编写 `sql` 语句和 `python` 脚本进行自定义搜索

## 快速开始

快速开始部分以 `Java` 运行时环境 `rt.jar` 为例进行分析，更具体的分析案例会在后续给出

以 `Windows` 环境下的 `JDK 8` 为例，可以从 `C:\Program Files\Java\jdk1.8.0_202\jre\lib` 下得到 `rt.jar` 文件

（1）使用 `build` 命令构建数据库

```shell
java -jar jar-analyzer-cli.jar build --jar rt.jar
```

输出如下，在我的机器配置下，构建 `rt.jar` 数据库需要 `34` 秒（这是已经对 `sql` 语句进行优化后的结果）

当构建完成后，你当前目录应该会有一个 `jar-analyzer.db` 文件

（2）查看内置模块

```shell
java -jar jar-analyzer-cli.jar analyze --list
```

输出如下：

```text
     ____.                 _____                .__
    |    |____ _______    /  _  \   ____ _____  |  | ___.__.________ ___________
    |    \__  \\_  __ \  /  /_\  \ /    \\__  \ |  |<   |  |\___   // __ \_  __ \
/\__|    |/ __ \|  | \/ /    |    \   |  \/ __ \|  |_\___  | /    /\  ___/|  | \/
\________(____  /__|    \____|__  /___|  (____  /____/ ____|/_____ \\___  >__|
              \/                \/     \/     \/     \/           \/    \/
┌────────────────────────┬────────────────────────────────────────────────────────┐
│module-name             │params                                                  │
├────────────────────────┼────────────────────────────────────────────────────────┤
│search-class-name       │[class-name]                                            │
├────────────────────────┼────────────────────────────────────────────────────────┤
│search-class-member     │[member-name]|[member-type]                             │
├────────────────────────┼────────────────────────────────────────────────────────┤
│search-member-in-class  │[class-name]                                            │
├────────────────────────┼────────────────────────────────────────────────────────┤
│search-method-callee    │[callee-class-name]|[callee-method-name]|[optional-desc]│
├────────────────────────┼────────────────────────────────────────────────────────┤
│search-method-caller    │[caller-class-name]|[caller-method-name]|[optional-desc]│
├────────────────────────┼────────────────────────────────────────────────────────┤
│search-method-name      │[method-name]                                           │
├────────────────────────┼────────────────────────────────────────────────────────┤
│search-spring-controller│none                                                    │
├────────────────────────┼────────────────────────────────────────────────────────┤
│search-spring-mapping   │none                                                    │
├────────────────────────┼────────────────────────────────────────────────────────┤
│search-string           │[string-name]                                           │
├────────────────────────┼────────────────────────────────────────────────────────┤
│search-string-in-class  │[class-name]                                            │
└────────────────────────┴────────────────────────────────────────────────────────┘
```

关于这些 `module` 进一步的说明：
- search-class-name: 输入完全类名得到这个类来自哪个 `jar` ，父类名信息（最基本的搜索）
- search-class-member: 输入成员名称和类型，搜索该属性在哪些类中可以找到
- search-member-in-class: 输入完全类名得到这个类中所有成员的信息
- search-method-callee: 根据 `callee` 类和方法名搜索所有 `caller` 信息（即搜索哪里调用了某个方法）
- search-method-caller: 根据 `caller` 类和方法名搜索所有 `callee` 信息（即搜索某个方法调用了哪些方法）
- search-method-name: 根据方法名搜索类名等信息（最基本的搜索）
- search-spring-controller: 搜索所有符合 `Spring Controller` 规范的类（通过注解信息）
- search-spring-mapping: 搜索所有符合 `Spring Mapping` 的方法（通过注解信息）
- search-string: 搜索哪些类中包含了指定的字符串常量内容
- search-string-in-class: 输入完全类名得到这个类中所有方法中包含的字符串常量信息

（3）执行查询

**示例一：搜索 `java/lang/Runtime` 类基本信息**

```shell
java -jar jar-analyzer-cli-0.0.1.jar analyze --db jar-analyzer.db --script search-class-name --input java/lang/Runtime --output result.txt
```

显示：

```text
jar_name:rt.jar   super_class_name:java/lang/Object   is_interface:false   class_name:java/lang/Runtime   
```

结果分析：
- `java/lang/Runtime` 类不是接口类型
- 来自 `rt.jar` 文件
- 父类是 `java/lang/Object`

**示例二：搜索 `java/util/HashMap` 类的所有成员**

```shell
java -jar jar-analyzer-cli-0.0.1.jar analyze --db jar-analyzer.db --script search-member-in-class --input java/util/HashMap --output result.txt
```

显示：

```text
type_class_name:[Ljava/util/HashMap$Node;   member_name:table   modifiers:transient   modifiers_int:128   class_name:java/util/HashMap   
type_class_name:java/util/Set   member_name:entrySet   modifiers:transient   modifiers_int:128   class_name:java/util/HashMap   
type_class_name:I   member_name:size   modifiers:transient   modifiers_int:128   class_name:java/util/HashMap   
type_class_name:I   member_name:modCount   modifiers:transient   modifiers_int:128   class_name:java/util/HashMap   
type_class_name:I   member_name:threshold   modifiers:   modifiers_int:0   class_name:java/util/HashMap   
type_class_name:F   member_name:loadFactor   modifiers:final   modifiers_int:16   class_name:java/util/HashMap   
```

结果分析：
- `java/util/HashMap` 类中包含了 `threshold` 等 6 个成员
- 例如 `size` 成员是 `int` 类型修饰符为 `transient`

**示例三：搜索哪些类里有 `I` 类型（`int`）且名称是 `size` 的成员**

```shell
java -jar jar-analyzer-cli-0.0.1.jar analyze --db jar-analyzer.db --script search-class-member --input "size|I" --output result.txt
```

显示：

```text
type_class_name:I   member_name:size   modifiers:   modifiers_int:0   class_name:com/sun/corba/se/impl/orbutil/CacheTable   
type_class_name:I   member_name:size   modifiers:private   modifiers_int:2   class_name:java/io/ObjectInputStream$HandleTable$HandleList   
type_class_name:I   member_name:size   modifiers:private   modifiers_int:2   class_name:java/util/TaskQueue   
type_class_name:I   member_name:size   modifiers:final   modifiers_int:16   class_name:java/util/ArraysParallelSortHelpers$FJByte$Sorter   
type_class_name:I   member_name:size   modifiers:final   modifiers_int:16   class_name:java/util/ArraysParallelSortHelpers$FJDouble$Sorter   
type_class_name:I   member_name:size   modifiers:private   modifiers_int:2   class_name:java/util/WeakHashMap   
type_class_name:I   member_name:size   modifiers:final   modifiers_int:16   class_name:java/util/ArraysParallelSortHelpers$FJInt$Sorter   
type_class_name:I   member_name:size   modifiers:private   modifiers_int:2   class_name:java/beans/WeakIdentityMap   
......
```

结果分析：
- 存在很多类有这样的属性

**示例四：搜索哪些方法调用了 `Runtime.exec` 方法**

```shell
java -jar jar-analyzer-cli-0.0.1.jar analyze --db jar-analyzer.db --script search-method-callee --input "java/lang/Runtime|exec" --output result.txt
```

显示：

```text
caller_method_name:exec   caller_class_name:java/lang/Runtime   caller_method_desc:(java.lang.String[]) -> java.lang.Process   caller_method_desc_native:([Ljava/lang/String;)Ljava/lang/Process;   
caller_method_name:exec   caller_class_name:java/lang/Runtime   caller_method_desc:(java.lang.String) -> java.lang.Process   caller_method_desc_native:(Ljava/lang/String;)Ljava/lang/Process;   
caller_method_name:registerUsage   caller_class_name:sun/usagetracker/UsageTrackerClient   caller_method_desc:(long) -> void   caller_method_desc_native:(J)V   
caller_method_name:exec   caller_class_name:java/lang/Runtime   caller_method_desc:(java.lang.String, java.lang.String[], java.io.File) -> java.lang.Process   caller_method_desc_native:(Ljava/lang/String;[Ljava/lang/String;Ljava/io/File;)Ljava/lang/Process;   
caller_method_name:run   caller_class_name:sun/net/www/MimeLauncher   caller_method_desc:() -> void   caller_method_desc_native:()V
.....
```

结果分析：
- 虽然找到了很多地方，但存在很多同名且 `desc` 不同的方法

那么如何精确到传入一个 `String` 返回 `Process` 的 `exec` 方法呢

```shell
java -jar jar-analyzer-cli-0.0.1.jar analyze --db jar-analyzer.db --script search-method-callee --input "java/lang/Runtime|exec|(Ljava/lang/String;)Ljava/lang/Process;" --output result.txt
```

显示：

```text
caller_method_name:registerUsage   caller_class_name:sun/usagetracker/UsageTrackerClient   caller_method_desc:(long) -> void   caller_method_desc_native:(J)V   
caller_method_name:run   caller_class_name:sun/net/www/MimeLauncher   caller_method_desc:() -> void   caller_method_desc_native:()V   
caller_method_name:activate   caller_class_name:com/sun/corba/se/impl/activation/ServerTableEntry   caller_method_desc:() -> void   caller_method_desc_native:()V   
caller_method_name:verify   caller_class_name:com/sun/corba/se/impl/activation/ServerTableEntry   caller_method_desc:() -> int   caller_method_desc_native:()I   
```

结果分析：
- 在 `rt.jar` 中找到了四处调用 `Runtime.exec` 且传入一个 `String` 的情况

**示例五：搜索 `com/sun/rowset/JdbcRowSetImpl` 类的 `connect` 方法中调用了哪些方法**

```shell
java -jar jar-analyzer-cli-0.0.1.jar analyze --db jar-analyzer.db --script search-method-caller --input "com/sun/rowset/JdbcRowSetImpl|connect" --output result.txt
```

显示：

```text
callee_method_name:getConnection   callee_method_desc:() -> java.sql.Connection   callee_method_desc_native:()Ljava/sql/Connection;   callee_class_name:javax/sql/DataSource   
callee_method_name:<init>   callee_method_desc:(java.lang.String) -> void   callee_method_desc_native:(Ljava/lang/String;)V   callee_class_name:java/sql/SQLException   
callee_method_name:getDataSourceName   callee_method_desc:() -> java.lang.String   callee_method_desc_native:()Ljava/lang/String;   callee_class_name:com/sun/rowset/JdbcRowSetImpl   
callee_method_name:toString   callee_method_desc:() -> java.lang.String   callee_method_desc_native:()Ljava/lang/String;   callee_class_name:java/lang/Object   
callee_method_name:getUsername   callee_method_desc:() -> java.lang.String   callee_method_desc_native:()Ljava/lang/String;   callee_class_name:com/sun/rowset/JdbcRowSetImpl   
callee_method_name:getUrl   callee_method_desc:() -> java.lang.String   callee_method_desc_native:()Ljava/lang/String;   callee_class_name:com/sun/rowset/JdbcRowSetImpl   
callee_method_name:getConnection   callee_method_desc:(java.lang.String, java.lang.String) -> java.sql.Connection   callee_method_desc_native:(Ljava/lang/String;Ljava/lang/String;)Ljava/sql/Connection;   callee_class_name:javax/sql/DataSource   
callee_method_name:handleGetObject   callee_method_desc:(java.lang.String) -> java.lang.Object   callee_method_desc_native:(Ljava/lang/String;)Ljava/lang/Object;   callee_class_name:com/sun/rowset/JdbcRowSetResourceBundle   
callee_method_name:lookup   callee_method_desc:(java.lang.String) -> java.lang.Object   callee_method_desc_native:(Ljava/lang/String;)Ljava/lang/Object;   callee_class_name:javax/naming/Context   
......
```

结果分析：
- 其中包含了 `javax/naming/Context` 的 `lookup` 方法，有潜在的安全问题

**示例六：搜索 `com/sun/rowset/JdbcRowSetImpl` 类包含了哪些字符串常量**

```shell
java -jar jar-analyzer-cli-0.0.1.jar analyze --db jar-analyzer.db --script search-string-in-class --input com/sun/rowset/JdbcRowSetImpl --output result.txt
```

结果过多不再展示

## 常见问题

**（1）是否支持更加自定义的查询？**

可以自行编写 `python` 脚本并加载后执行查询

这是一个脚本模板：

```python
from me.n1ar4.analyze import db_instance

print("[python] script-input:  " + str(input))
print("[python] script-output: " + str(output))

db = db_instance()
sql = "SELECT COUNT(1) FROM sqlite_master"
result = db.execute_sql(sql)

for item in list(result):
    i = dict(item)
    for k, v in i.items():
        print(k, v)
```

使用命令：

```shell
java -jar jar-analyzer-cli-0.0.1.jar analyze --db jar-analyzer.db --file test.py --input test --output test
```

控制台输出：

```text
     ____.                 _____                .__
    |    |____ _______    /  _  \   ____ _____  |  | ___.__.________ ___________
    |    \__  \\_  __ \  /  /_\  \ /    \\__  \ |  |<   |  |\___   // __ \_  __ \
/\__|    |/ __ \|  | \/ /    |    \   |  \/ __ \|  |_\___  | /    /\  ___/|  | \/
\________(____  /__|    \____|__  /___|  (____  /____/ ____|/_____ \\___  >__|
              \/                \/     \/     \/     \/           \/    \/
17:39:22 [main] AnalyzerRunner.start analyze script file: D:\JavaCode\jar-analyzer-cli\test.py
17:39:22 [main] AnalyzerRunner.start run script: D:\JavaCode\jar-analyzer-cli\test.py
[python] script-input:  test
[python] script-output: test
(u'COUNT(1)', 11)
17:39:22 [main] AnalyzerRunner.start close python interpreter success
```

注意：
- `input` 和 `output` 参数从外部注入，通过命令行传递
- `db_instance` 函数根据传入的数据库名称连接
- **运行脚本不需要本地有 `python` 环境**，本项目内置了 `jython`

**（1）怎样支持多个 `jar` 文件构建数据库？**

命令支持传入一个 `jar` 目录

示例：加载 `Tomcat` 所有依赖 `jar` 进行分析

```shell
java -jar jar-analyzer-cli-0.0.1.jar build --jar "D:\apache-tomcat-8.5.85\lib"
```

搜索其中存在 `readObject` 调用的地方

```shell
java -jar jar-analyzer-cli-0.0.1.jar analyze --db jar-analyzer.db --script search-method-callee --input "java/io/ObjectInputStream|readObject" --output result.txt
```

显示：

```text
caller_method_name:readObject   caller_class_name:org/apache/tomcat/dbcp/pool2/impl/LinkedBlockingDeque   caller_method_desc:(java.io.ObjectInputStream) -> void   caller_method_desc_native:(Ljava/io/ObjectInputStream;)V   
caller_method_name:readObject   caller_class_name:org/apache/catalina/authenticator/SingleSignOnEntry   caller_method_desc:(java.io.ObjectInputStream) -> void   caller_method_desc_native:(Ljava/io/ObjectInputStream;)V   
caller_method_name:deserializeSessions   caller_class_name:org/apache/catalina/ha/session/DeltaManager   caller_method_desc:(byte[]) -> void   caller_method_desc_native:([B)V   
caller_method_name:doReadObject   caller_class_name:org/apache/catalina/session/StandardSession   caller_method_desc:(java.io.ObjectInputStream) -> void   caller_method_desc_native:(Ljava/io/ObjectInputStream;)V   
caller_method_name:doLoad   caller_class_name:org/apache/catalina/session/StandardManager   caller_method_desc:() -> void   caller_method_desc_native:()V   
caller_method_name:deserialize   caller_class_name:org/apache/catalina/tribes/io/XByteBuffer   caller_method_desc:(byte[], int, int, java.lang.ClassLoader[]) -> java.io.Serializable   caller_method_desc_native:([BII[Ljava/lang/ClassLoader;)Ljava/io/Serializable;   
caller_method_name:deserialize   caller_class_name:org/apache/tomcat/dbcp/dbcp2/datasources/InstanceKeyDataSourceFactory   caller_method_desc:(byte[]) -> java.lang.Object   caller_method_desc_native:([B)Ljava/lang/Object;   
```

这里面包含了 `CVE-2020-9484 Tomcat Session RCE` 的漏洞触发点（已修复）

## Thanks

<img src="https://resources.jetbrains.com/storage/products/company/brand/logos/IntelliJ_IDEA.svg" alt="IntelliJ IDEA logo.">


This project is developed using JetBrains IDEA.
Thanks to JetBrains for providing me with a free license, which is a strong support for me.