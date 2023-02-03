package cn.letout.mybatis.scripting.xmltags;

import cn.letout.mybatis.executor.parameter.ParameterHandler;
import cn.letout.mybatis.mapping.BoundSql;
import cn.letout.mybatis.mapping.MappedStatement;
import cn.letout.mybatis.mapping.SqlSource;
import cn.letout.mybatis.scripting.LanguageDriver;
import cn.letout.mybatis.scripting.defaults.DefaultParameterHandler;
import cn.letout.mybatis.session.Configuration;
import org.dom4j.Element;

/**
 * XML 语言驱动器
 * 封装对 XMLScriptBuilder 的调用处理
 */
public class XMLLanguageDriver implements LanguageDriver {

    @Override
    public SqlSource createSqlSource(Configuration configuration, Element script, Class<?> parameterType) {
        // 用 XML 脚本构建器解析
        XMLScriptBuilder builder = new XMLScriptBuilder(configuration, script, parameterType);
        return builder.parseScriptNode();
    }

    @Override
    public ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        return new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
    }

}
