package cn.letout.mybatis.mapping;

import cn.letout.mybatis.scripting.LanguageDriver;
import cn.letout.mybatis.session.Configuration;

import java.util.List;

/**
 * 映射语句类
 * 用于记录 SQL 信息：SQL 类型、SQL 语句、入参类型、出参类型
 */
public class MappedStatement {

    private Configuration configuration;

    private String id;  // 接口的全限定名 cn.letout.mybatis.dao.IUserDao.queryUserInfoById

    private SqlCommandType sqlCommandType;  // enum -> SELECT

    private SqlSource sqlSource;  // SQL 信息 eg: staticSqlSource

    private Class<?> resultType;  // 返回类型

    private LanguageDriver lang;

    private List<ResultMap> resultMaps;


    MappedStatement() {
    }

    public static class Builder {

        private MappedStatement mappedStatement = new MappedStatement();

        public Builder(Configuration configuration, String id, SqlCommandType sqlCommandType, SqlSource sqlSource, Class<?> resultType) {
            mappedStatement.configuration = configuration;
            mappedStatement.id = id;
            mappedStatement.sqlCommandType = sqlCommandType;
            mappedStatement.sqlSource = sqlSource;
            mappedStatement.resultType = resultType;
            mappedStatement.lang = configuration.getDefaultScriptingLanguageInstance();
        }

        public MappedStatement build() {
            assert mappedStatement.configuration != null;
            assert mappedStatement.id != null;
            return mappedStatement;
        }

        public String id() {
            return mappedStatement.id;
        }

        public Builder resultMaps(List<ResultMap> resultMaps) {
            mappedStatement.resultMaps = resultMaps;
            return this;
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public String getId() {
        return id;
    }

    public SqlCommandType getSqlCommandType() {
        return sqlCommandType;
    }

    public SqlSource getSqlSource() {
        return sqlSource;
    }

    public Class<?> getResultType() {
        return resultType;
    }

    public LanguageDriver getLang() {
        return lang;
    }

    public List<ResultMap> getResultMaps() {
        return resultMaps;
    }

    //

    public BoundSql getBoundSql(Object parameterObject) {
        return sqlSource.getBoundSql(parameterObject);
    }

}
