package cn.letout.mybatis.scripting.xmltags;

import java.util.List;

public class MixedSqlNode implements SqlNode {

    // 组合模式
    private List<SqlNode> contents;

    public MixedSqlNode(List<SqlNode> contents) {
        this.contents = contents;
    }

    @Override
    public boolean apply(DynamicContext context) {
        // 依次调用 list 里的每个元素的 apply
        contents.forEach(node -> node.apply(context));
        return true;
    }

}
