package cn.letout.mybatis.scripting;

import cn.letout.mybatis.mapping.SqlSource;
import cn.letout.mybatis.session.Configuration;

import org.dom4j.Element;

/**
 * 语言脚本驱动
 *
 * 提供创建 SQL 信息的方法
 */
public interface LanguageDriver {

    SqlSource createSqlSource(Configuration configuration, Element script, Class<?> parameterType);

}
