package cn.letout.mybatis.reflection.invoker;

import java.lang.reflect.Field;

public class SetFieldInvoker implements Invoker {

    private Field field;

    public SetFieldInvoker(Field field) {
        this.field = field;
    }

    @Override
    public Object invoke(Object target, Object[] args) throws Exception {
        field.set(target, args[0]);
        return null;  // set 只是设置值，直接返回 null
    }

    @Override
    public Class<?> getType() {
        return field.getType();
    }
}
