package cn.letout.mybatis.reflection.invoker;

import java.lang.reflect.Field;

/**
 * getter 调用者
 */
public class GetFieldInvoker implements Invoker {

    private Field field;

    public GetFieldInvoker(Field field) {
        this.field = field;
    }

    // get 是有返回值的，直接对 Field 字段操作完后，直接返回结果
    @Override
    public Object invoke(Object target, Object[] args) throws Exception {
        return field.get(target);
    }

    @Override
    public Class<?> getType() {
        return field.getType();
    }

}
