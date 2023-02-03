package cn.letout.mybatis.builder.xml;

import cn.letout.mybatis.builder.BaseBuilder;
import cn.letout.mybatis.mapping.MappedStatement;
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

    private String currentNamespace;  // cn.letout.mybatis.dao.IUserDao

    private Element element;

    public XMLStatementBuilder(Configuration configuration, Element element, String currentNamespace) {
        super(configuration);
        this.element = element;
        this.currentNamespace = currentNamespace;
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

        // 结果类型
        String resultType = element.attributeValue("resultType");  // cn.letout.mybatis.po.User
        Class<?> resultTypeClass = resolveAlias(resultType);

        // 命令类型 select / insert / update / delete
        String nodeName = element.getName();
        SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));

        // 获取默认语言驱动器
        Class<?> langClass = configuration.getLanguageRegistry().getDefaultDriverClass();
        LanguageDriver langDriver = configuration.getLanguageRegistry().getDriver(langClass);

        SqlSource sqlSource = langDriver.createSqlSource(configuration, element, parameterTypeClass);  // StaticSqlSource

        MappedStatement mappedStatement = new MappedStatement.Builder(
                configuration,
                currentNamespace + "." + id,  // cn.letout.mybatis.dao.IUserDao.queryUserInfoById
                sqlCommandType,
                sqlSource,
                resultTypeClass
        ).build();

        // 将解析完的语句添加到 Configuration 配置文件中的 Map<String, MappedStatement> 中
        configuration.addMappedStatement(mappedStatement);
    }

}
