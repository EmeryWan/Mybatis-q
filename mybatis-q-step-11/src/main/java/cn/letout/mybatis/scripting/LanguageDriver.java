package cn.letout.mybatis.scripting;

import cn.letout.mybatis.executor.parameter.ParameterHandler;
import cn.letout.mybatis.mapping.BoundSql;
import cn.letout.mybatis.mapping.MappedStatement;
import cn.letout.mybatis.mapping.SqlSource;
import cn.letout.mybatis.session.Configuration;

import org.dom4j.Element;

/**
 * 语言脚本驱动
 *
 * 提供创建 SQL 信息的方法
 */
public interface LanguageDriver {

    /**
     * mapper xml 方式 创建 SQL 源码
     */
    SqlSource createSqlSource(Configuration configuration, Element script, Class<?> parameterType);

    /**
     * 创建参数处理器
     */
    ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql);

}
