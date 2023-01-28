## Mapper XML 的解析和注册使用


### ❓ 问题

- 添加 Mapper.xml 文件，将 namespace、sql 及相关参数都再 XML 中配置，而不是硬编码
- 通过解析 XML
  - 完成 Mapper 的注册（注册代理对象工厂，用于创建 Mapper 的动态代理对象）
  - SQL 相关信息的管理


### 🎨 设计

![](../imgs/03/1.png)

- 定义 `SqlSessionFactoryBuilder` 工厂建造者模式类，通过入口 IO 的方式对 XML 文件进行解析。

- 文件解析以后会存放到 `Configuration` 配置类中，配置类 `Configuration` 会被串联到整个 Mybatis 流程中，所有内容存放和读取都离不开这个类。

### 💡 结果

`SqlSessionFactoryBuilder` 的引入包装了整个执行过程，包括：XML 文件的解析、Configuration 配置类的处理，
让 `DefaultSqlSession` 可以更加灵活的拿到对应的信息，获取 Mapper 和 SQL 语句。

