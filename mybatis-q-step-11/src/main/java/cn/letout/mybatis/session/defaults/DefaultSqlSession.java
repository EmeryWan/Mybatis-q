package cn.letout.mybatis.session.defaults;

import cn.letout.mybatis.executor.Executor;
import cn.letout.mybatis.mapping.MappedStatement;
import cn.letout.mybatis.session.Configuration;
import cn.letout.mybatis.session.RowBounds;
import cn.letout.mybatis.session.SqlSession;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DefaultSqlSession implements SqlSession {

    private Logger log = LoggerFactory.getLogger(DefaultSqlSession.class);

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

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        log.info("执行查询 statement：{} parameter：{}", statement, JSON.toJSONString(parameter));
        MappedStatement ms = configuration.getMappedStatement(statement);
        List<T> list = executor.query(ms, parameter, RowBounds.DEFAULT, Executor.NO_RESULT_HANDLER, ms.getSqlSource().getBoundSql(parameter));
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
