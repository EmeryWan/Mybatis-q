package cn.letout.mybatis.builder.xml;

import cn.letout.mybatis.builder.BaseBuilder;
import cn.letout.mybatis.datasource.DataSourceFactory;
import cn.letout.mybatis.io.Resources;
import cn.letout.mybatis.mapping.Environment;
import cn.letout.mybatis.session.Configuration;
import cn.letout.mybatis.transaction.TransactionFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import javax.sql.DataSource;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Properties;

/**
 * XML 配置构建器
 * 核心在于：初始化 Configuration
 * 解析内容：(1) Mappers (2) Environments
 */
public class XMLConfigBuilder extends BaseBuilder {

    private Element root;

    public XMLConfigBuilder(Reader reader) {
        // 1. 初始化 Configuration
        super(new Configuration());

        // 2. dom4j 处理 XML
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(new InputSource(reader));
            root = document.getRootElement();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析配置
     * 全部包括：类型别名、插件、对象工厂、对象包装工厂、设置、环境、类型转换、映射器
     * 这里暂时只做一些 SQL 解析处理
     */
    public Configuration parse() {
        try {
            // 解析环境
            environmentsElement(root.element("environments"));
            // 解析映射器
            mapperElement(root.element("mappers"));
        } catch (Exception e) {
            throw new RuntimeException("Error parsing SQL Mapper Configuration. Cause: " + e, e);
        }

        return configuration;
    }

    /*
     * <environments default="development">
         * <environment id="development">
             * <transactionManager type="JDBC">
                 * <property name="..." value="..."/>
             * </transactionManager>
             * <dataSource type="POOLED">
                 * <property name="driver" value="${driver}"/>
                 * <property name="url" value="${url}"/>
                 * <property name="username" value="${username}"/>
                 * <property name="password" value="${password}"/>
             * </dataSource>
         * </environment>
     * </environments>
     */
    private void environmentsElement(Element context) throws Exception {
        String environment = context.attributeValue("default");

        List<Element> environmentList = context.elements("environment");
        for (Element e : environmentList) {
            String id = e.attributeValue("id");
            if (environment.equals(id)) {
                // 解析 & ->事务管理器
                TransactionFactory txFactory = (TransactionFactory) typeAliasRegistry.resolveAlias(
                        e.element("transactionManager").attributeValue("type")
                ).newInstance();

                // 解析 & ->数据源
                Element dataSourceElement = e.element("dataSource");
                DataSourceFactory dataSourceFactory = (DataSourceFactory) typeAliasRegistry.resolveAlias(
                        dataSourceElement.attributeValue("type")
                ).newInstance();
                List<Element> propertyList = dataSourceElement.elements("property");
                Properties props = new Properties();
                for (Element property : propertyList) {
                    props.setProperty(property.attributeValue("name"), property.attributeValue("value"));
                }
                dataSourceFactory.setProperties(props);
                DataSource dataSource = dataSourceFactory.getDataSource();

                // 构建环境
                Environment.Builder environmentBuilder = new Environment.Builder(id)
                        .transactionFactory(txFactory)
                        .dataSource(dataSource);

                configuration.setEnvironment(environmentBuilder.build());
            }
        }
    }


    /* 将原来的流程解耦化
     * <mappers>
     *	 <mapper resource="org/mybatis/builder/AuthorMapper.xml"/>
     *	 <mapper resource="org/mybatis/builder/BlogMapper.xml"/>
     *	 <mapper resource="org/mybatis/builder/PostMapper.xml"/>
     * </mappers>
     */
    private void mapperElement(Element mappers) throws Exception {
        List<Element> mapperList = mappers.elements("mapper");
        for (Element e : mapperList) {
            String resource = e.attributeValue("resource");
            InputStream inputStream = Resources.getResourceAsStream(resource);

            XMLMapperBuilder mapperParse = new XMLMapperBuilder(inputStream, configuration, resource);
            mapperParse.parse();
        }
    }

}
