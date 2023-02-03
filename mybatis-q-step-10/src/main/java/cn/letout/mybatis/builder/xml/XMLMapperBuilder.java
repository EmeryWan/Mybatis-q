package cn.letout.mybatis.builder.xml;

import cn.letout.mybatis.builder.BaseBuilder;
import cn.letout.mybatis.io.Resources;
import cn.letout.mybatis.session.Configuration;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

/**
 * XML 映射器构建器
 */
public class XMLMapperBuilder extends BaseBuilder {

    private Element element;

    private String resource;

    private String currentNamespace;


    public XMLMapperBuilder(Document document, Configuration configuration, String resource) {
        super(configuration);
        this.element = document.getRootElement();
        this.resource = resource;
    }

    public XMLMapperBuilder(InputStream inputStream, Configuration configuration, String resource) throws DocumentException {
        this(new SAXReader().read(inputStream), configuration, resource);
    }

    public void parse() throws Exception {
        // 如果当前资源没有加载过再加载
        // 防止重复加载
        if (!configuration.isResourceLoaded(resource)) {
            configurationElement(element);
            // 标记一下已经加载
            configuration.addLoadedResource(resource);
            // 将 namespace 绑定到 Mapper 上
            configuration.addMapper(Resources.classForName(currentNamespace));
        }
    }

    // 配置mapper元素
    // <mapper namespace="org.mybatis.example.BlogMapper">
    //   <select id="selectBlog" parameterType="int" resultType="Blog">
    //    select * from Blog where id = #{id}
    //   </select>
    // </mapper>
    private void configurationElement(Element element) {
        // 配置 namespace
        currentNamespace = element.attributeValue("namespace");
        if (currentNamespace.equals("")) {
            throw new RuntimeException("Mapper's namespace cannot be empty");
        }

        // 配置 select / insert / update / delete
        buildStatementFromContext(element.elements("select"));
    }

    private void buildStatementFromContext(List<Element> list) {
        for (Element element : list) {
            final XMLStatementBuilder statementParse = new XMLStatementBuilder(configuration, element, currentNamespace);
            statementParse.parseStatementNode();
        }
    }

}
