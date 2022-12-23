package cn.letout.mybatis.builder.xml;

import cn.letout.mybatis.builder.BaseBuilder;
import cn.letout.mybatis.io.Resources;
import cn.letout.mybatis.mapping.MappedStatement;
import cn.letout.mybatis.mapping.SqlCommandType;
import cn.letout.mybatis.session.Configuration;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * XML 配置的构建器，使用构建者模式
 * 核心在于：初始化 Configuration
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
            throw new RuntimeException(e);
        }
    }

    //

    /**
     * 解析配置
     * 全部包括：类型别名、插件、对象工厂、对象包装工厂、设置、环境、类型转换、映射器
     * 这里暂时只做一些 SQL 解析处理
     */
    public Configuration parse() {
        try {
            // 解析映射器
            mapperElement(root.element("mappers"));
        } catch (Exception e) {
            throw new RuntimeException("Error parsing SQL Mapper Configuration. Cause: " + e, e);
        }
        return configuration;
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
                MappedStatement mappedStatement = new MappedStatement.Builder(
                        configuration, msId, sqlCommandType, parameterType, resultType, sql, parameter
                ).build();

                // 添加解析 SQL
                configuration.addMappedStatement(mappedStatement);
            }

            // 注册 Mapper 映射器
            configuration.addMapper(Resources.classForName(namespace));
        }
    }

}
