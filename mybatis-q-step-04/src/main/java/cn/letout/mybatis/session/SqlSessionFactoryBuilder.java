package cn.letout.mybatis.session;

import cn.letout.mybatis.builder.xml.XMLConfigBuilder;
import cn.letout.mybatis.session.defaults.DefaultSqlSessionFactory;

import java.io.Reader;

/**
 * SqlSessionFactoryBuilder 构建 SqlSessionFactory 的工厂
 * 是整个 MyBatis 的入口，通过解析指定 XML 的 IO，引导整个流程的启动
 * （1）提供创建者工厂
 * （2）包装 XML 解析处理
 * （3）返回对应的 SqlSessionFactory 处理类
 */
public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(Configuration config) {
        return new DefaultSqlSessionFactory(config);
    }

    public SqlSessionFactory build(Reader reader) {
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(reader);
        return build(xmlConfigBuilder.parse());
    }

}
