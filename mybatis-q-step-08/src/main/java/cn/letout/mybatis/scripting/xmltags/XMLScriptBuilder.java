package cn.letout.mybatis.scripting.xmltags;

import cn.letout.mybatis.builder.BaseBuilder;
import cn.letout.mybatis.mapping.SqlSource;
import cn.letout.mybatis.scripting.defaults.RawSqlSource;
import cn.letout.mybatis.session.Configuration;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * XML 脚本构建器
 */
public class XMLScriptBuilder extends BaseBuilder {

    private Element element;

    private boolean isDynamic;

    private Class<?> parameterType;

    public XMLScriptBuilder(Configuration configuration, Element element, Class<?> parameterType) {
        super(configuration);
        this.element = element;
        this.parameterType = parameterType;
    }

    public SqlSource parseScriptNode() {
        List<SqlNode> contents = parseDynamicTags(element);
        MixedSqlNode rootSqlNode = new MixedSqlNode(contents);
        return new RawSqlSource(configuration, rootSqlNode, parameterType);
    }

    public List<SqlNode> parseDynamicTags(Element element) {
        List<SqlNode> contents = new ArrayList<>();
        String data = element.getText(); // 拿到 SQL
        contents.add(new StaticTextSqlNode(data));
        return contents;
    }

}
