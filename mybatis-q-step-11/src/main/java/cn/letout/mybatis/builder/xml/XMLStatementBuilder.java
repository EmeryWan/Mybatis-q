package cn.letout.mybatis.builder.xml;

import cn.letout.mybatis.builder.BaseBuilder;
import cn.letout.mybatis.builder.MapperBuilderAssistant;
import cn.letout.mybatis.mapping.SqlCommandType;
import cn.letout.mybatis.mapping.SqlSource;
import cn.letout.mybatis.scripting.LanguageDriver;
import cn.letout.mybatis.session.Configuration;
import org.dom4j.Element;

import java.util.Locale;

/**
 * XML 语句构建器
 */
public class XMLStatementBuilder extends BaseBuilder {

    private MapperBuilderAssistant builderAssistant;

    private Element element;

    public XMLStatementBuilder(Configuration configuration, Element element, MapperBuilderAssistant builderAssistant) {
        super(configuration);
        this.element = element;
        this.builderAssistant = builderAssistant;
    }

    // 解析语句(select|insert|update|delete)
    // <select
    //   id="queryUserInfoById"
    //   parameterType="java.lang.Long"
    //   parameterMap="deprecated"
    //   resultType="hashmap"
    //   resultMap="personResultMap"
    //   flushCache="false"
    //   useCache="true"
    //   timeout="10000"
    //   fetchSize="256"
    //   statementType="PREPARED"
    //   resultSetType="cn.letout.mybatis.po.User"
    // >
    //   SELECT * FROM PERSON WHERE ID = #{id}
    //</select>
    public void parseStatementNode() {
        String id = element.attributeValue("id");

        // 参数类型
        String parameterType = element.attributeValue("parameterType");  // java.lang.Long
        Class<?> parameterTypeClass = resolveAlias(parameterType);

        // 结果类型 resultMap
        String resultMap = element.attributeValue("resultMap");

        // 结果类型 resultType
        String resultType = element.attributeValue("resultType");  // cn.letout.mybatis.po.User
        Class<?> resultTypeClass = resolveAlias(resultType);

        // 命令类型 select / insert / update / delete
        String nodeName = element.getName();
        SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));

        // 获取默认语言驱动器
        Class<?> langClass = configuration.getLanguageRegistry().getDefaultDriverClass();
        LanguageDriver langDriver = configuration.getLanguageRegistry().getDriver(langClass);

        // 解析成SqlSource，DynamicSqlSource/RawSqlSource
        SqlSource sqlSource = langDriver.createSqlSource(configuration, element, parameterTypeClass);  // StaticSqlSource

        // 调用助手类处理，便于统一处理参数的包装
        builderAssistant.addMappedStatement(
                id,
                sqlSource,
                sqlCommandType,
                parameterTypeClass,
                resultMap,
                resultTypeClass,
                langDriver
        );
    }

}
