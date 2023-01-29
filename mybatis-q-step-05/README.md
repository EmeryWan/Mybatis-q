## 数据源池化技术实现

之前的实现中，利用了 Druid 创建数据源完成数据库的操作。在 MyBatis 中是有两种数据源实现：

- UnpooledDataSource
- PooledDataSource

池化技术是将一些创建成本较高且使用高频的资源，进行预热并缓存处理。
将这些资源放到一个“池”中，进行统一配置，需要的时候直接获取，使用完后再放回。


```
mybatis-q-step-05
└── src
    ├── main
    │   └── java
    │       └── cn.letout.mybatis
    │           ├── binding
    │           ├── builder
    │           ├── datasource
    │           │   ├── druid
    │           │   │   └── DruidDataSourceFactory.java
    │           │   ├── pooled
    │           │   │   ├── PooledConnection.java
    │           │   │   ├── PooledDataSource.java
    │           │   │   ├── PooledDataSourceFactory.java
    │           │   │   └── PoolState.java
    │           │   ├── unpooled
    │           │   │   ├── UnpooledDataSource.java
    │           │   │   └── UnpooledDataSourceFactory.java
    │           │   └── DataSourceFactory.java
    │           ├── io
    │           ├── mapping
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
    │           └── type
    └── test
```