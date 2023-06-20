# Jar Analyzer Cli
![](https://img.shields.io/badge/build-passing-brightgreen)
![](https://img.shields.io/badge/build-Java%208-orange)
![](https://img.shields.io/github/downloads/4ra1n/jar-analyzer-cli/total)
![](https://img.shields.io/github/v/release/4ra1n/jar-analyzer-cli)

## Introduction

This project is the command-line version of [Jar Analyzer](https://github.com/4ra1n/jar-analyzer), which is easier to use and allows for better customization of analysis and search.

How it works:
- Automatically analyzes the content of all `class` files and builds a database based on the input `jar` file or `jars` directory.
- Built-in `jython` interpreter and commonly used search scripts allow for simple commands to search the constructed database.
- If the built-in scripts do not meet your needs, you can write your own scripts and execute queries through the `jython` interpreter.

Compared to the `Jar Analyzer GUI` version, which saves all analysis results in an in-memory `HashMap`, the command-line version saves analysis data in an `sqlite` database, making it more meaningful. For example, it can achieve the effect of **building the database once and analyzing it multiple times**, or customizing search using `sql` statements and `python` scripts.

## Quick Start

This quick start section uses the `rt.jar` file in the `Java` runtime environment as an example for analysis. More specific analysis cases will be provided later.

For example, in `JDK 8` under `Windows`, you can find the `rt.jar` file in `C:\Program Files\Java\jdk1.8.0_202\jre\lib`.

(1) Use the `build` command to build the database.

```shell
java -jar jar-analyzer-cli.jar build --jar rt.jar
```

On my machine configuration, building the `rt.jar` database takes `34` seconds (this is the result after optimizing the `sql` statement).

When the build is complete, you should have a `jar-analyzer.db` file in your current directory.

(2) View built-in modules

```shell
java -jar jar-analyzer-cli.jar analyze --list
```

Output:

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

Further explanation on these `modules`:
- `search-class-name`: Input the fully qualified class name to get information on which `jar` the class comes from and its parent class name (the most basic search).
- `search-class-member`: Input the member name and type to search for which classes contain this property.
- `search-member-in-class`: Input the fully qualified class name to get information on all members in this class.
- `search-method-callee`: Search for all `caller` information based on the `callee` class and method name (i.e., search where a method is called).
- `search-method-caller`: Search for all `callee` information based on the `caller` class and method name (i.e., search which methods are called by a method).
- `search-method-name`: Search for class names and other information based on method names (the most basic search).
- `search-spring-controller`: Search for all classes that meet the `Spring Controller` specification (through annotation information).
- `search-spring-mapping`: Search for all methods that meet the `Spring Mapping` specification (through annotation information).
- `search-string`: Search for which classes contain specified string constant content.
- `search-string-in-class`: Input the fully qualified class name to get information on all string constants contained in this class's methods.

(3) Perform the query

**Example 1: Search for basic information on the `java/lang/Runtime` class.**

```shell
java -jar jar-analyzer-cli-0.0.1.jar analyze --db jar-analyzer.db --script search-class-name --input java/lang/Runtime --output result.txt
```

Output:

```text
jar_name:rt.jar   super_class_name:java/lang/Object   is_interface:false   class_name:java/lang/Runtime   
```

Analysis of the result:
- The `java/lang/Runtime` class is not an interface type.
- It comes from the `rt.jar` file.
- Its parent class is `java/lang/Object`.

**Example 2: Search for all members of the `java/util/HashMap` class.**

```shell
java -jar jar-analyzer-cli-0.0.1.jar analyze --db jar-analyzer.db --script search-member-in-class --input java/util/HashMap --output result.txt
```

Output:

```text
type_class_name:[Ljava/util/HashMap$Node;   member_name:table   modifiers:transient   modifiers_int:128   class_name:java/util/HashMap   
type_class_name:java/util/Set   member_name:entrySet   modifiers:transient   modifiers_int:128   class_name:java/util/HashMap   
type_class_name:I   member_name:size   modifiers:transient   modifiers_int:128   class_name:java/util/HashMap   
type_class_name:I   member_name:modCount   modifiers:transient   modifiers_int:128   class_name:java/util/HashMap   
type_class_name:I   member_name:threshold   modifiers:   modifiers_int:0   class_name:java/util/HashMap   
type_class_name:F   member_name:loadFactor   modifiers:final   modifiers_int:16   class_name:java/util/HashMap   
```

Result Analysis:
- The class `java/util/HashMap` has 6 members, among which `threshold` is included.
- For instance, the member `size` is of type `int` and is declared with the modifier `transient`.

**Example 3: Search for classes that have a member of type `int` and named `size`.**

```shell
java -jar jar-analyzer-cli-0.0.1.jar analyze --db jar-analyzer.db --script search-class-member --input "size|I" --output result.txt
```

Output:

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

Result Analysis:
- There are many classes that have such properties.

**Example 4: Search for methods that call the `Runtime.exec` method.**

```shell
java -jar jar-analyzer-cli-0.0.1.jar analyze --db jar-analyzer.db --script search-method-callee --input "java/lang/Runtime|exec" --output result.txt
```

Output:

```text
caller_method_name:exec   caller_class_name:java/lang/Runtime   caller_method_desc:(java.lang.String[]) -> java.lang.Process   caller_method_desc_native:([Ljava/lang/String;)Ljava/lang/Process;   
caller_method_name:exec   caller_class_name:java/lang/Runtime   caller_method_desc:(java.lang.String) -> java.lang.Process   caller_method_desc_native:(Ljava/lang/String;)Ljava/lang/Process;   
caller_method_name:registerUsage   caller_class_name:sun/usagetracker/UsageTrackerClient   caller_method_desc:(long) -> void   caller_method_desc_native:(J)V   
caller_method_name:exec   caller_class_name:java/lang/Runtime   caller_method_desc:(java.lang.String, java.lang.String[], java.io.File) -> java.lang.Process   caller_method_desc_native:(Ljava/lang/String;[Ljava/lang/String;Ljava/io/File;)Ljava/lang/Process;   
caller_method_name:run   caller_class_name:sun/net/www/MimeLauncher   caller_method_desc:() -> void   caller_method_desc_native:()V
.....
```

Result Analysis:
- Though many places have been found, there are many methods with the same name but different `desc`.

How to precisely search for the `exec` method that takes a `String` as input and returns a `Process` object?

```shell
java -jar jar-analyzer-cli-0.0.1.jar analyze --db jar-analyzer.db --script search-method-callee --input "java/lang/Runtime|exec|(Ljava/lang/String;)Ljava/lang/Process;" --output result.txt
```

Output:

```text
caller_method_name:registerUsage   caller_class_name:sun/usagetracker/UsageTrackerClient   caller_method_desc:(long) -> void   caller_method_desc_native:(J)V   
caller_method_name:run   caller_class_name:sun/net/www/MimeLauncher   caller_method_desc:() -> void   caller_method_desc_native:()V   
caller_method_name:activate   caller_class_name:com/sun/corba/se/impl/activation/ServerTableEntry   caller_method_desc:() -> void   caller_method_desc_native:()V   
caller_method_name:verify   caller_class_name:com/sun/corba/se/impl/activation/ServerTableEntry   caller_method_desc:() -> int   caller_method_desc_native:()I   
```

Result Analysis:
- Four cases have been found in `rt.jar` where `Runtime.exec` is called with a `String` argument.

**Example 5: Search for methods called within the `connect` method of the class `com/sun/rowset/JdbcRowSetImpl`.**

```shell
java -jar jar-analyzer-cli-0.0.1.jar analyze --db jar-analyzer.db --script search-method-caller --input "com/sun/rowset/JdbcRowSetImpl|connect" --output result.txt
```

Output:

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

Result Analysis:
- The `lookup` method of `javax/naming/Context` is included, which has potential security concerns.

**Example 6: Search for string constants contained in the class `com/sun/rowset/JdbcRowSetImpl`.**

```shell
java -jar jar-analyzer-cli-0.0.1.jar analyze --db jar-analyzer.db --script search-string-in-class --input com/sun/rowset/JdbcRowSetImpl --output result.txt
```

## Issues

**(1) Do you support more customized queries?**

Sure, you can write your own Python script and load it to execute the query.

Here's a script template:

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

Command:

```shell
java -jar jar-analyzer-cli-0.0.1.jar analyze --db jar-analyzer.db --file test.py --input test --output test
```

Output:

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

Noted:
- The `input` and `output` parameters are injected from outside and passed through the command line.
- The `db_instance` function connects to the database based on the given database name.
- Running the script does not require a local `Python` environment since `Jython` is included in the project.

**(2) How to support building a database with multiple jar files?**

The command supports passing in a directory of jar files.

Example: Load all dependent jar files of Tomcat for analysis.

```shell
java -jar jar-analyzer-cli-0.0.1.jar build --jar "D:\apache-tomcat-8.5.85\lib"
```

You can use the following command to search for places where readObject is called:

```shell
java -jar jar-analyzer-cli-0.0.1.jar analyze --db jar-analyzer.db --script search-method-callee --input "java/io/ObjectInputStream|readObject" --output result.txt
```

Output:

```text
caller_method_name:readObject   caller_class_name:org/apache/tomcat/dbcp/pool2/impl/LinkedBlockingDeque   caller_method_desc:(java.io.ObjectInputStream) -> void   caller_method_desc_native:(Ljava/io/ObjectInputStream;)V   
caller_method_name:readObject   caller_class_name:org/apache/catalina/authenticator/SingleSignOnEntry   caller_method_desc:(java.io.ObjectInputStream) -> void   caller_method_desc_native:(Ljava/io/ObjectInputStream;)V   
caller_method_name:deserializeSessions   caller_class_name:org/apache/catalina/ha/session/DeltaManager   caller_method_desc:(byte[]) -> void   caller_method_desc_native:([B)V   
caller_method_name:doReadObject   caller_class_name:org/apache/catalina/session/StandardSession   caller_method_desc:(java.io.ObjectInputStream) -> void   caller_method_desc_native:(Ljava/io/ObjectInputStream;)V   
caller_method_name:doLoad   caller_class_name:org/apache/catalina/session/StandardManager   caller_method_desc:() -> void   caller_method_desc_native:()V   
caller_method_name:deserialize   caller_class_name:org/apache/catalina/tribes/io/XByteBuffer   caller_method_desc:(byte[], int, int, java.lang.ClassLoader[]) -> java.io.Serializable   caller_method_desc_native:([BII[Ljava/lang/ClassLoader;)Ljava/io/Serializable;   
caller_method_name:deserialize   caller_class_name:org/apache/tomcat/dbcp/dbcp2/datasources/InstanceKeyDataSourceFactory   caller_method_desc:(byte[]) -> java.lang.Object   caller_method_desc_native:([B)Ljava/lang/Object;   
```

This search result includes the trigger point for the `CVE-2020-9484 Tomcat Session RCE` vulnerability (which has been fixed).

## Thanks

<img src="https://resources.jetbrains.com/storage/products/company/brand/logos/IntelliJ_IDEA.svg" alt="IntelliJ IDEA logo.">


This project is developed using JetBrains IDEA.
Thanks to JetBrains for providing me with a free license, which is a strong support for me.