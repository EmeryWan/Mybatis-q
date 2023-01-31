package cn.letout.mybatis.scripting.defaults;

import cn.letout.mybatis.builder.SqlSourceBuilder;
import cn.letout.mybatis.mapping.BoundSql;
import cn.letout.mybatis.mapping.SqlSource;
import cn.letout.mybatis.scripting.xmltags.DynamicContext;
import cn.letout.mybatis.scripting.xmltags.SqlNode;
import cn.letout.mybatis.session.Configuration;

import java.util.HashMap;

/**
 * 原始 SQL 源码
 */
public class RawSqlSource implements SqlSource {

    private final SqlSource sqlSource;  // StaticSqlSource

    /**
     * 在构造方法中，会实例化 SqlSourceBuilder，将 RawSqlSource -> StaticSqlSource
     */
    public RawSqlSource(Configuration configuration, SqlNode rootSqlNode, Class<?> parameterType) {
        this(configuration, getSql(configuration, rootSqlNode), parameterType);
    }

    public RawSqlSource(Configuration configuration, String sql, Class<?> parameterType) {
        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration);

        Class<?> clazz = parameterType == null ? Object.class : parameterType;
        sqlSource = sqlSourceParser.parse(sql, clazz, new HashMap<>());
    }


    //


    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        return sqlSource.getBoundSql(parameterObject);
    }

    private static String getSql(Configuration configuration, SqlNode rootSqlNode) {
        DynamicContext context = new DynamicContext(configuration, null);
        rootSqlNode.apply(context);
        return context.getSql();
    }

}
