package cn.letout.mybatis.session.defaults;

import cn.letout.mybatis.binding.MapperRegistry;
import cn.letout.mybatis.session.Configuration;
import cn.letout.mybatis.session.SqlSession;
import cn.letout.mybatis.session.SqlSessionFactory;

/**
 * 默认的 SqlSessionFactory
 *
 * 传递 mapperRegistry 创建 DefaultSession，就可以在使用 SqlSession 时获取每个代理类的映射器对象
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }

}
