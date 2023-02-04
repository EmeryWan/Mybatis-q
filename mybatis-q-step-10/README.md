## 解耦流程，封装结果集处理器

核心：拿到 Mapper XML 中所配置的返回类型，解析后把数据库查询到的结构，通过反射类型实例化的对象上。


### 设计

#### 定义出参对象

- `ResultMap`

结果映射类。使用 id、type 用于包装返回结果。

- `MapperBuilderAssistant`

映射器构建助手类，用于构建 MappedStatement。在 MappedStatement 中，封装了入参映射、出参映射、将配置信息写入 Configuration 中

将 Mapper XML 中配置的返回值类型 `resultType` 封装成 ResultMap 对象，按照职责细分解耦，使得返回值类型能够被统一为 ResultMap 对象（这部分在初始化解析 XML 文件时实现）。


#### 封装结果查询

之前的实现中，封装结果是通过遍历 SQL 查询的结果集，进行强制类型转换为相应的对象（如 User），封装结果查询的核心在于：使用反射工具类，动态地将结果封装成相应的对象。


```
mybatis-q-step-10
└── src
    ├── main
    │   └── java
    │       └── cn.letout.mybatis
    │           ├── binding
    │           ├── builder
    │           │   ├── xml
    │           │   │   ├── XMLConfigBuilder.java
    │           │   │   ├── XMLMapperBuilder.java
    │           │   │   └── XMLStatementBuilder.java
    │           │   ├── BaseBuilder.java
    │           │   ├── MapperBuilderAssistant.java  # 为创建 MappedStatement 服务
    │           │   ├── ParameterExpression.java
    │           │   ├── SqlSourceBuilder.java
    │           │   └── StaticSqlSource.java
    │           ├── datasource
    │           ├── executor
    │           │   ├── parameter
    │           │   │   └── ParameterHandler.java
    │           │   ├── result
    │           │   │   ├── DefaultResultContext.java 
    │           │   │   └── DefaultResultHandler.java  # implement ResultHandler 默认的结果处理器
    │           │   ├── resultset
    │           │   │   ├── DefaultResultSetHandler.java
    │           │   │   └── ResultSetHandler.java
    │           │   │   └── ResultSetWrapper.java  # 结果集包装器
    │           │   ├── statement
    │           │   │   ├── BaseStatementHandler.java
    │           │   │   ├── PreparedStatementHandler.java
    │           │   │   ├── SimpleStatementHandler.java
    │           │   │   └── StatementHandler.java
    │           │   ├── BaseExecutor.java
    │           │   ├── Executor.java
    │           │   └── SimpleExecutor.java
    │           ├── io
    │           ├── mapping
    │           │   ├── BoundSql.java
    │           │   ├── Environment.java
    │           │   ├── MappedStatement.java
    │           │   ├── ParameterMapping.java
    │           │   ├── ResultMap.java  # 结果映射封装
    │           │   ├── ResultMapping.java  # 结果类型的映射，类似于参数的 ParameterMapping
    │           │   ├── SqlCommandType.java
    │           │   └── SqlSource.java
    │           ├── parsing
    │           ├── reflection
    │           ├── scripting
    │           │   ├── defaults
    │           │   │   ├── DefaultParameterHandler.java
    │           │   │   └── RawSqlSource.java
    │           │   ├── xmltags
    │           │   │   ├── DynamicContext.java
    │           │   │   ├── MixedSqlNode.java
    │           │   │   ├── SqlNode.java
    │           │   │   ├── StaticTextSqlNode.java
    │           │   │   ├── XMLLanguageDriver.java
    │           │   │   └── XMLScriptBuilder.java
    │           │   ├── LanguageDriver.java
    │           │   └── LanguageDriverRegistry.java
    │           ├── session
    │           │   ├── defaults
    │           │   │   ├── DefaultSqlSession.java
    │           │   │   └── DefaultSqlSessionFactory.java
    │           │   ├── Configuration.java
    │           │   ├── ResultContext.java  # 结果上下文
    │           │   ├── ResultHandler.java  # 结果处理器接口，定义标准
    │           │   ├── RowBounds.java  # 分页设置
    │           │   ├── SqlSession.java
    │           │   ├── SqlSessionFactory.java
    │           │   ├── SqlSessionFactoryBuilder.java
    │           │   └── TransactionIsolationLevel.java
    │           ├── transaction
    │           └── type
    │               ├── BaseTypeHandler.java
    │               ├── JdbcType.java
    │               ├── LongTypeHandler.java
    │               ├── StringTypeHandler.java
    │               ├── TypeAliasRegistry.java
    │               ├── TypeHandler.java
    │               └── TypeHandlerRegistry.java
    └── test
```