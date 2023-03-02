package cn.letout.mybatis.scripting;

import java.util.HashMap;
import java.util.Map;

/**
 * 脚本语言注册器
 */
public class LanguageDriverRegistry {

    /**
     * eg:
     * key -> class cn.letout.mybatis.scripting.xmltags.XMLLanguageDriver
     * value ->
     */
    private final Map<Class<?>, LanguageDriver> LANGUAGE_DRIVER_MAP = new HashMap<>();

    /**
     * 会注册多个脚本语言
     * 默认为：class cn.letout.mybatis.scripting.xmltags.XMLLanguageDriver
     */
    private Class<?> defaultDriverClass = null;

    public void register(Class<?> cls) {
        if (cls == null) {
            throw new IllegalArgumentException("null is not a valid Language Driver");
        }
        if (!LanguageDriver.class.isAssignableFrom(cls)) {
            throw new RuntimeException(cls.getName() + " does not implements " + LanguageDriver.class.getName());
        }
        // 如果没注册过，再去注册
        LanguageDriver driver = LANGUAGE_DRIVER_MAP.get(cls);
        if (driver == null) {
            try {
                // 单例模式，即一个 Class 只有一个对应的 LanguageDriver
                driver = (LanguageDriver) cls.newInstance();
                LANGUAGE_DRIVER_MAP.put(cls, driver);
            } catch (Exception ex) {
                throw new RuntimeException("Failed to load language driver for " + cls.getName(), ex);
            }
        }
    }

    public LanguageDriver getDriver(Class<?> cls) {
        return LANGUAGE_DRIVER_MAP.get(cls);
    }

    public LanguageDriver getDefaultDriver() {
        return getDriver(getDefaultDriverClass());
    }

    public Class<?> getDefaultDriverClass() {
        return defaultDriverClass;
    }

    //Configuration()有调用，默认的为XMLLanguageDriver
    public void setDefaultDriverClass(Class<?> defaultDriverClass) {
        register(defaultDriverClass);
        this.defaultDriverClass = defaultDriverClass;
    }

}
