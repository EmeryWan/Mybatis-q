package cn.letout.mybatis.mapping;

/**
 * SQL 源码
 */
public interface SqlSource {

    BoundSql getBoundSql(Object parameterObject);

}
