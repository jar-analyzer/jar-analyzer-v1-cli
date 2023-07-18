# Jar Analyzer Cli
![](https://img.shields.io/badge/build-passing-brightgreen)
![](https://img.shields.io/badge/build-Java%208-orange)
![](https://img.shields.io/github/downloads/4ra1n/jar-analyzer-cli/total)
![](https://img.shields.io/github/v/release/4ra1n/jar-analyzer-cli)

## Introduction

This project is a command-line version of [Jar Analyzer ↗](https://github.com/4ra1n/jar-analyzer), which is easier to use and provides better customization for analysis and search.

This project can turn one or multiple `jar` files into a `sqlite` database, which can be analyzed with self-written `sql` statements.

## Quick Start

Build the `rt.jar` database (takes less than a minute):

```shell
java -jar jar-analyzer-cli.jar build --jar "/path/to/rt.jar"
```

Build the `weblogic` database (takes several minutes):

```shell
java -jar jar-analyzer-cli.jar build --jar "/path/to/Oracle/wlserver/modules"
```

The tables in the database include:
- anno_table: annotation table
- class_file_table: class file location table
- class_table: class information table
- interface_table: interface table
- jar_table: jar file table
- member_table: class member variable table
- method_call_table: method call table
- method_impl_table: method implementation table
- method_table: method information table

## Thanks

<img src="https://resources.jetbrains.com/storage/products/company/brand/logos/IntelliJ_IDEA.svg" alt="IntelliJ IDEA logo.">


This project is developed using JetBrains IDEA.
Thanks to JetBrains for providing me with a free license, which is a strong support for me.