## 数据源的解析、创建和使用，定义事务框架


- 解析配置
- 建立事务框架
- 引入 DRUID 连接池
- 初步完成 SQL 的执行 和 结果简单包装

![](../imgs/04/1.png)

```
mybatis-q-step-04
└── src
    ├── main
    │   └── java
    │       └── cn.letout.mybatis
    │           ├── binding
    │           │   ├── MapperMethod.java
    │           │   ├── MapperProxy.java
    │           │   ├── MapperProxyFactory.java
    │           │   └── MapperRegistry.java
    │           ├── builder
    │           │   ├── xml
    │           │   │   └── XMLConfigBuilder.java  # 在之前的实现上，添加解析数据源配置
    │           │   └── BaseBuilder.java
    │           ├── datasource
    │           │   ├── druid
    │           │   │   └── DruidDataSourceFactory.java  # 利用 XML 数据源配置 和 Druid 构建数据源 dataSource（也就是与数据库的连接）
    │           │   └── DataSourceFactory.java  # interface 数据源工厂
    │           ├── io
    │           │   └── Resources.java
    │           ├── mapping
    │           │   ├── BoundSql.java  # 解析格式化好的 SQL 语句的封装
    │           │   ├── Environment.java  # 根据 XML 中解析的数据源信息生成的环境 dataSource transactionFactory
    │           │   ├── MappedStatement.java  # 记录 SQL 信息：SQL 类型，语句、入参、出参
    │           │   ├── ParameterMapping.java  # 参数映射 jdbc type <---> java type
    │           │   └── SqlCommandType.java
    │           ├── session
    │           │   ├── defaults
    │           │   │   ├── DefaultSqlSession.java
    │           │   │   └── DefaultSqlSessionFactory.java
    │           │   ├── Configuration.java
    │           │   ├── SqlSession.java
    │           │   ├── SqlSessionFactory.java
    │           │   ├── SqlSessionFactoryBuilder.java
    │           │   └── TransactionIsolationLevel.java  # enum 事务隔离级别
    │           ├── transaction
    │           │   ├── jdbc
    │           │   │   ├── JdbcTransaction.java  # JDBC 事务的实现
    │           │   │   └── JdbcTransactionFactory.java
    │           │   ├── Transaction.java  # interface 定义标注的事务的接口，具体实现交给不同的事务方式。获取连接、提交、回滚、关闭。
    │           │   └── TransactionFactory.java  # interface 事务工厂。可以通过参数，打开不同配置的事务（如隔离级别、自动提交等）
    │           └── type
    │               ├── JdbcType.java  # enum jdbc 的数据类型
    │               └── TypeAliasRegistry.java  # 类型别名注册器，注册各种类型->Class，获取类型对应的 Class 类别
    └── test
```