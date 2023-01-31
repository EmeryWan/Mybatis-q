## SQL 执行器的定义与实现

在之前的实现中，`DefaultSqlSession` 中直接硬编码了数据库的操作：获取数据源、执行 SQL、封装结果。

我们需要单独提供执行方法的入口，才能更改的应对和扩展不同的需求变化：入参，结果封装，执行器类型，批处理等。



### 🎨 设计


之前的 SQL 执行都耦合到了 SqlSession 中，一旦需要进行扩展，就需要修改 SqlSession，为了降低耦合，将执行 SQL 的逻辑拆分到 SQL Executor。


利用模板模式，在 接口（`Executor` `StatementHandler`） 中定义标准流程，并使用 抽象类（`BaseExecutor` `BaseStatementHandler`） 完成通用操作，
具体的实现交给 子类（`SimpleExecutor` `SimpleStatementHandler` `PreparedStatementHandler`） 实现。

![](../imgs/06/1.png)


- 执行器 `Executor`
  
  - 执行器负责：封装事务、与数据源建立连接、执行方法等
  - abstract class BaseExecutor，实现接口中的所有通用方法，具体的增删改查实现，由子类自定义实现
  - 具体实现 SimpleExecutor，继承抽象方法，实现数据库的操作

- 语句处理器 `StatementHandler`

  - 负责：准备语句、设置参数、执行 SQL、封装结果。同样使用模板方法模式
  - SimpleStatementHandler 负责处理没有参数的简单 SQL
  - PreparedStatementHandler 有参数 SQL 的处理器，使用 PreparedStatement 设置 SQL，传递参数的设置
  - 最终的结果封装在 ResultSetHandler


```
mybatis-q-step-06
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
    │           ├── datasource
    │           ├── executor
    │           │   ├── resultset
    │           │   │   ├── DefaultResultSetHandler.java  # 结果处理器默认实现
    │           │   │   └── ResultSetHandler.java  # 结果处理器
    │           │   ├── statement
    │           │   │   ├── BaseStatementHandler.java  # abstract class 模板模式
    │           │   │   ├── PreparedStatementHandler.java  # 最常用的 StatementHandler 的具体实现之一，PreparedStatement 设置 SQL，传递参数的设置
    │           │   │   ├── SimpleStatementHandler.java  # StatementHandler 的具体实现之一，处理没有参数的简单语句处理器
    │           │   │   └── StatementHandler.java  # 语句处理器标准：准备语句、参数化传递、执行 SQL、封装结果
    │           │   ├── BaseExecutor.java  # abstract class 完成一些执行器的通用方法（模板模式）
    │           │   ├── Executor.java  # interface 执行器标准的流程定义：执行方法、事务获取、提交、回滚、关闭
    │           │   └── SimpleExecutor.java  # 简单执行器的实现
    │           ├── io
    │           ├── mapping
    │           ├── session
    │           │   ├── defaults
    │           │   │   ├── DefaultSqlSession.java
    │           │   │   └── DefaultSqlSessionFactory.java
    │           │   ├── Configuration.java
    │           │   ├── ResultHandler.java
    │           │   ├── SqlSession.java
    │           │   ├── SqlSessionFactory.java
    │           │   ├── SqlSessionFactoryBuilder.java
    │           │   └── TransactionIsolationLevel.java
    │           ├── transaction
    │           └── type
    └── test
```
