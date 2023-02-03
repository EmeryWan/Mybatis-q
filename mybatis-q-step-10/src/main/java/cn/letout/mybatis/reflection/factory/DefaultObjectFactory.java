package cn.letout.mybatis.reflection.factory;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;

public class DefaultObjectFactory implements ObjectFactory, Serializable {

    private static final long serialVersionUID = -8855120656740914948L;

    @Override
    public void setProperties(Properties properties) {

    }

    @Override
    public <T> T create(Class<T> type) {
        return null;
    }

    @Override
    public <T> T create(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
        return null;
    }

    @Override
    public <T> boolean isCollection(Class<T> type) {
        return false;
    }

}
