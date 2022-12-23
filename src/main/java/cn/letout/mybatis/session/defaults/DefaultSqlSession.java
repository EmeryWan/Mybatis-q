package cn.letout.mybatis.session.defaults;

import cn.letout.mybatis.mapping.MappedStatement;
import cn.letout.mybatis.session.Configuration;
import cn.letout.mybatis.session.SqlSession;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 暂时先简单返回
     * 目前没有和数据库进行关联，后续实现
     */
    @Override
    public <T> T selectOne(String statement) {
        return (T) ("你被代理了！方法：" + statement);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        MappedStatement mappedStatement = configuration.getMappedStatement(statement);
        return (T) ("你被代理了！" +
                "\n方法：" + statement +
                "\n入参：" + parameter +
                "\n待执行 SQL: " + mappedStatement.getSql()
        );
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return configuration.getMapper(type, this);
    }
}
