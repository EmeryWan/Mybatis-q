package cn.letout.mybatis.builder.xml;

import cn.letout.mybatis.builder.BaseBuilder;
import cn.letout.mybatis.datasource.DataSourceFactory;
import cn.letout.mybatis.io.Resources;
import cn.letout.mybatis.mapping.BoundSql;
import cn.letout.mybatis.mapping.Environment;
import cn.letout.mybatis.mapping.MappedStatement;
import cn.letout.mybatis.mapping.SqlCommandType;
import cn.letout.mybatis.session.Configuration;
import cn.letout.mybatis.transaction.TransactionFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import javax.sql.DataSource;
import java.io.Reader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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


    /**
     * 具体解析操作
     * 把解析后的信息，通过 Configuration 配置类进行存放
     * 包括：添加解析 SQL、注册 Mapper 映射器
     */
    private void mapperElement(Element mappers) throws Exception {
        // 获得所有 <mapper> -> resource 中配置的 Mapper.xml 文件地址
        List<Element> mapperList = mappers.elements("mapper");
        for (Element e : mapperList) {
            String resource = e.attributeValue("resource");  // <mapper resource="mapper/UserMapper.xml"/>
            Reader reader = Resources.getResourceAsReader(resource);
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(new InputSource(reader));
            Element root = document.getRootElement();

            // 命名空间 (cn.letout.mybatis.dao.IUserDao)
            String namespace = root.attributeValue("namespace");

            // SELECT
            List<Element> selectNodes = root.elements("select");
            for (Element node : selectNodes) {
                String id = node.attributeValue("id");  // queryUserInfoById
                String parameterType = node.attributeValue("parameterType");  // java.lang.Long
                String resultType = node.attributeValue("resultType");  // cn.letout.mybatis.po.User
                String sql = node.getText();

                // 将 #{} 解析为参数占位符 ? （Mybatis $->直接字符串替换 #->解析为预编译语句?）
                Map<Integer, String> parameter = new HashMap<>();
                Pattern pattern = Pattern.compile("(#\\{(.*?)})");
                Matcher matcher = pattern.matcher(sql);
                for (int i = 1; matcher.find(); i++) {
                    String g1 = matcher.group(1);
                    String g2 = matcher.group(2);
                    parameter.put(i, g2);
                    sql = sql.replace(g1, "?");
                }

                String msId = namespace + "." + id;  // cn.letout.mybatis.dao.IUserDao.queryUserInfoById
                String nodeName = node.getName();  // select
                SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));  // SELECT
                BoundSql boundSql = new BoundSql(sql, parameter, parameterType, resultType);

                MappedStatement mappedStatement = new MappedStatement.Builder(configuration, msId, sqlCommandType, boundSql).build();

                // 添加解析 SQL
                configuration.addMappedStatement(mappedStatement);
            }

            // 注册 Mapper 映射器
            configuration.addMapper(Resources.classForName(namespace));
        }
    }

}
