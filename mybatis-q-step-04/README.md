## 数据源的解析、创建和使用


- 解析配置
- 建立事务框架
- 引入 DRUID 连接池
- 初步完成 SQL 的执行 和 结果简单包装


```
mybatis-q-step-04
└── src
    ├── main
    │   └── java
    │       └── cn.bugstack.mybatis
    │           ├── binding
    │           │   ├── MapperMethod.java
    │           │   ├── MapperProxy.java
    │           │   ├── MapperProxyFactory.java
    │           │   └── MapperRegistry.java
    │           ├── builder
    │           │   ├── xml
    │           │   │   └── XMLConfigBuilder.java
    │           │   └── BaseBuilder.java
    │           ├── datasource
    │           │   ├── druid
    │           │   │   └── DruidDataSourceFactory.java
    │           │   └── DataSourceFactory.java
    │           ├── io
    │           │   └── Resources.java
    │           ├── mapping
    │           │   ├── BoundSql.java
    │           │   ├── Environment.java
    │           │   ├── MappedStatement.java
    │           │   ├── ParameterMapping.java
    │           │   └── SqlCommandType.java
    │           ├── session
    │           │   ├── defaults
    │           │   │   ├── DefaultSqlSession.java
    │           │   │   └── DefaultSqlSessionFactory.java
    │           │   ├── Configuration.java
    │           │   ├── SqlSession.java
    │           │   ├── SqlSessionFactory.java
    │           │   ├── SqlSessionFactoryBuilder.java
    │           │   └── TransactionIsolationLevel.java
    │           ├── transaction
    │           │   ├── jdbc
    │           │   │   ├── JdbcTransaction.java
    │           │   │   └── JdbcTransactionFactory.java
    │           │   ├── Transaction.java
    │           │   └── TransactionFactory.java
    │           └── type
    │               ├── JdbcType.java
    │               └── TypeAliasRegistry.java
    └── test

```