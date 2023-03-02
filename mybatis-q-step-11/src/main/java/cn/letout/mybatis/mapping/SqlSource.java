package cn.letout.mybatis.mapping;

/**
 * SQL 源
 *
 * 解析后的内容都被封装在 SqlSource 中，根据 XML 中语句的类型 / 用途 有不同的实现类，记录 SQL 的信息
 * 从 SqlSource 中获取 BoundSql
 */
public interface SqlSource {

    BoundSql getBoundSql(Object parameterObject);

}
