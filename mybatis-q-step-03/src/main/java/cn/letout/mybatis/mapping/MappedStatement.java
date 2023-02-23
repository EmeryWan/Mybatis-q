package cn.letout.mybatis.mapping;

import cn.letout.mybatis.session.Configuration;

import java.util.Map;

/**
 * 被映射的语句类
 * SQL 信息记录对象，用于记录 SQL 信息：SQL 类型、SQL 语句、入参类型、出参类型
 */
public class MappedStatement {

    private Configuration configuration;

    private String id;

    private SqlCommandType sqlCommandType;

    private String parameterType;

    private String resultType;

    private String sql;

    private Map<Integer, String> parameter;  // 参数，暂时以简单的方式，后续完善

    // 禁用构造
    MappedStatement() {
    }

    public static class Builder {

        private MappedStatement mappedStatement = new MappedStatement();

        public Builder(Configuration configuration, String id,
                       SqlCommandType sqlCommandType, String parameterType, String resultType,
                       String sql, Map<Integer, String> parameter) {
            mappedStatement.configuration = configuration;
            mappedStatement.id = id;
            mappedStatement.sqlCommandType = sqlCommandType;
            mappedStatement.parameterType = parameterType;
            mappedStatement.resultType = resultType;
            mappedStatement.sql = sql;
            mappedStatement.parameter = parameter;
        }

        public MappedStatement build() {
            assert mappedStatement.configuration != null;
            assert mappedStatement.id != null;
            return mappedStatement;
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SqlCommandType getSqlCommandType() {
        return sqlCommandType;
    }

    public void setSqlCommandType(SqlCommandType sqlCommandType) {
        this.sqlCommandType = sqlCommandType;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Map<Integer, String> getParameter() {
        return parameter;
    }

    public void setParameter(Map<Integer, String> parameter) {
        this.parameter = parameter;
    }
}
