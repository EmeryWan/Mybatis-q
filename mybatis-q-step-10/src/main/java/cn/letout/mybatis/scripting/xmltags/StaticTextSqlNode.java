package cn.letout.mybatis.scripting.xmltags;

/**
 * 静态文本 SQL 节点
 */
public class StaticTextSqlNode implements SqlNode {

    private String text;

    public StaticTextSqlNode(String text) {
        this.text = text;
    }

    @Override
    public boolean apply(DynamicContext context) {
        context.appendSql(text);  // 将文本加入 context
        return true;
    }
}
