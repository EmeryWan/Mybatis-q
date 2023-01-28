package cn.letout.mybatis.builder;

import cn.letout.mybatis.mapping.BoundSql;
import cn.letout.mybatis.mapping.ParameterMapping;
import cn.letout.mybatis.mapping.SqlSource;
import cn.letout.mybatis.session.Configuration;

import java.util.List;

/**
 * 静态 SQL 源码
 */
public class StaticSqlSource implements SqlSource {

    private Configuration configuration;

    private String sql;

    private List<ParameterMapping> parameterMappings;


    //


    public StaticSqlSource(Configuration configuration, String sql) {
        this(configuration, sql, null);
    }

    public StaticSqlSource(Configuration configuration, String sql, List<ParameterMapping> parameterMappings) {
        this.configuration = configuration;
        this.sql = sql;
        this.parameterMappings = parameterMappings;
    }


    //


    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        return new BoundSql(configuration, sql, parameterMappings, parameterObject);
    }

}
