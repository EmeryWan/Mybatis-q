package cn.letout.mybatis.session.defaults;

import cn.letout.mybatis.binding.MapperRegistry;
import cn.letout.mybatis.executor.Executor;
import cn.letout.mybatis.mapping.BoundSql;
import cn.letout.mybatis.mapping.Environment;
import cn.letout.mybatis.mapping.MappedStatement;
import cn.letout.mybatis.session.Configuration;
import cn.letout.mybatis.session.SqlSession;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    private Executor executor;

    public DefaultSqlSession(Configuration configuration, Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    @Override
    public <T> T selectOne(String statement) {
        return this.selectOne(statement, null);
    }

    /**
     * 暂时先简单返回
     */
    @Override
    public <T> T selectOne(String statement, Object parameter) {
        MappedStatement ms = configuration.getMappedStatement(statement);
        List<T> list = executor.query(ms, parameter, Executor.NO_RESULT_HANDLER, ms.getBoundSql());
        return list.get(0);
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
