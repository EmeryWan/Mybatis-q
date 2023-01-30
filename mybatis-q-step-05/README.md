## 数据源池化技术实现

之前的实现中，利用了 Druid 创建数据源完成数据库的操作。在 MyBatis 中是有两种数据源实现：

- 无池化 UnpooledDataSource
- 池化 PooledDataSource

池化技术是将一些创建成本较高且使用高频的资源，进行预热并缓存处理。
将这些资源放到一个“池”中，进行统一配置，需要的时候直接获取，使用完后再放回。


### 🛣️ UnpooledDataSource

每次获取无池化的数据源连接，都需要新建连接对象。

核心在于 UnpooledDataSource 中 initializerDriver 初始化驱动中使用 Class.forName 和 newInstance 创建数据源连接操作。

适用于简单的应用程序，不依赖于数据库的性能。


### 🏞️ PooledDataSource

核心思想：将无池化方式创建的数据源连接对象收集起来，存入活跃的连接池（`PoolState -> List<PooledConnection> activeConnections`）；如果某个
连接对象被使用完，可以将该连接移入空闲连接池（`PoolState -> List<PooledConnection> idleConnections`），以提高资源的利用。 


- 🧩 `popConnection` 获取连接对象

多个线程竞争获取连接池的资源，通过 while 不断自旋，直到根据当前情况获取到连接。

在竞争时，需要操作同时 PoolState 对象，使用 synchronized 锁住 `PoolState state`，防止多线程写操作带来的不一致。

  - 👣 如果空闲连接池 idleConnections List 中有连接对象，直接返回第一个连接
  - 👣 如果当前没有空闲连接，且活跃连接池 activeConnections List 的连接数 < 最大连接数，创建新的连接
  - 👣 如果活跃连接池已满，获取活跃连接池中的最老的连接（也就是第一个）。如果这个连接超过了最大 checkout 时间（执行时间超过了最大执行时间，说明该事务中可能出现了问题），记录信息，回滚该事务，并去除该连接；重新新建一个连接对象，给新来的任务
  - 👣 如果活跃连接池已满，不存在执行过程超时的连接，进行休眠一会（同时可被 notifyAll 唤醒），进行下一轮的循环
  - 👣 循环获取连接对象的过程中，会记录信息，如果多次都没有拿到连接对象，抛出异常处理



- 🧩 `pushConnection` 回收连接对象

同样存在多线程竞争的情况，需要锁住 `PoolState state` 对象。

  - 👣 将该连接对象从 activeConnections List 中进行移除
  - 👣 判断该连接是否有效（利用 pingConnection 侦测连接对象）
  - 👣 如果连接有效，判断连接空闲列表 idleConnections 连接数 < 最大空闲数，将该连接放入空闲列表（并不是直接放入，需要将该连接的相关信息重置到新创建的时候），并通过 notifyAll 唤醒所有等待的线程
  - 👣 如果空闲列表中的连接对象充足，将该连接关闭


- 🧩 `forceCloseAll`

关闭并清空两个连接池，同时用于初始化时创建空的连接池

- 🧩 `pingConnection`

发送 `NO PING QUERY SET` 侦测，验证连接是否有效


### PooledConnection

池化连接和普通连接的区别在于：池化连接对象在使用完后，需要归还连接池，而不是直接关闭。这里使用动态代理的方式管理池化连接资源。

PooledConnection implement InvocationHandler 的 invoke 方法，在其中使用 pushConnection 收回连接对象。

实例化 PoolConnection 对象时，就会为其创建代理连接 proxyConnection = Proxy.newProxyInstance()，当用于使用 proxyConnection 时，就会被反射拦截执行 invoke()，对方法增强回收连接（调用 close 时）。



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
    │           │   │   ├── PooledConnection.java  # 利用动态代理管理池化连接，增强方法来回收连接
    │           │   │   ├── PooledDataSource.java  # 池化连接数据源，其中定义了一些连接池相关的参数，以及获取连接、回收连接、侦测连接等相关操作
    │           │   │   ├── PooledDataSourceFactory.java
    │           │   │   └── PoolState.java  # 连接池的状态（空闲连接 List、活跃连接 List、请求次数、累计请求时间、累计检查时间 ...）
    │           │   ├── unpooled
    │           │   │   ├── UnpooledDataSource.java  # 无池化数据源
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