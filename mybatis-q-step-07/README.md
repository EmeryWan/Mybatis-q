## 添加反射相关工具

定义框架中的反射相关的工具类，了解即可。

是 MyBatis 中方便地处理属性创建的工具类，类似于 Hutool Guava，了解即可。

达到对象所提供的属性进行统一的设置和过去值的操作，与被处理的对象进行解耦。

![](../imgs/07/1.png)

```
mybatis-step-07
└── src
    ├── main
    │   └── java
    │       └── cn.bugstack.mybatis
    │           ├── binding
    │           ├── builder
    │           ├── datasource
    │           ├── executor
    │           ├── io
    │           ├── mapping
    │           ├── reflection  # 工具类
    │           │   ├── factory
    │           │   │   ├── DefaultObjectFactory.java
    │           │   │   └── ObjectFactory.java
    │           │   ├── invoker
    │           │   │   ├── GetFieldInvoker.java
    │           │   │   ├── Invoker.java
    │           │   │   ├── MethodInvoker.java
    │           │   │   └── SetFieldInvoker.java
    │           │   ├── property
    │           │   │   ├── PropertyNamer.java
    │           │   │   └── PropertyTokenizer.java
    │           │   ├── wrapper
    │           │   │   ├── BaseWrapper.java
    │           │   │   ├── BeanWrapper.java
    │           │   │   ├── CollectionWrapper.java
    │           │   │   ├── DefaultObjectWrapperFactory.java
    │           │   │   ├── MapWrapper.java
    │           │   │   ├── ObjectWrapper.java
    │           │   │   └── ObjectWrapperFactory.java
    │           │   ├── MetaClass.java
    │           │   ├── MetaObject.java
    │           │   ├── Reflector.java
    │           │   └── SystemMetaObject.java
    │           ├── session
    │           ├── transaction
    │           └── type
    └── test
```