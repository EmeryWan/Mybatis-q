package cn.letout.mybatis.builder;

import cn.letout.mybatis.session.Configuration;

/**
 * 构建者模式
 * 构建器的基类
 */
public abstract class BaseBuilder {

    protected final Configuration configuration;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

}
