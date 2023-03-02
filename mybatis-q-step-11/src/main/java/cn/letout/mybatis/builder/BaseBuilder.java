package cn.letout.mybatis.builder;

import cn.letout.mybatis.session.Configuration;
import cn.letout.mybatis.type.TypeAliasRegistry;
import cn.letout.mybatis.type.TypeHandlerRegistry;

public abstract class BaseBuilder {

    protected final Configuration configuration;

    protected final TypeAliasRegistry typeAliasRegistry;

    protected final TypeHandlerRegistry typeHandlerRegistry;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
        this.typeAliasRegistry = this.configuration.getTypeAliasRegistry();
        this.typeHandlerRegistry = this.configuration.getTypeHandlerRegistry();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    protected Class<?> resolveAlias(String alias) {
        return typeAliasRegistry.resolveAlias(alias);
    }

}
