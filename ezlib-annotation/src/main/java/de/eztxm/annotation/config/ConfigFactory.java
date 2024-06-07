package de.eztxm.annotation.config;

import de.eztxm.annotation.Config;
import de.eztxm.config.JsonConfig;
import de.eztxm.simplelogger.SimpleLogger;
import de.eztxm.config.object.ObjectConverter;

import java.lang.reflect.Method;

public class ConfigFactory {
    private final Class<?> clazz;
    private ConfigType type;

    private JsonConfig jsonConfig;

    public ConfigFactory(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> create() {
        if (!clazz.isAnnotation()) {
            return null;
        }
        Config annotation = clazz.getAnnotation(Config.class);
        type = annotation.type();
        String path = annotation.path();
        String fileName = annotation.fileName();
        switch (type) {
            case JSON -> jsonConfig = new JsonConfig(path, fileName);
            default -> new SimpleLogger("[ezLib] ").warn("This type of annotation config is currently not supported");
        }
        return clazz;
    }

    public void set(String key, Object value) {
        for (Method method : clazz.getMethods()) {
            if (method.getName().startsWith("set") && method.getName().toLowerCase().endsWith(key.toLowerCase()) && method.getReturnType().equals(Void.class)) {
                switch (type) {
                    case JSON -> jsonConfig.set(key, value);
                    default -> new SimpleLogger("[ezLib]").warn("This type of annotation config is currently not supported");
                }
                break;
            }
        }
    }

    public ObjectConverter get(String key) {
        for (Method method : clazz.getMethods()) {
            if (method.getName().startsWith("get") && method.getName().toLowerCase().endsWith(key.toLowerCase()) && !method.getReturnType().equals(Void.class)) {
                ObjectConverter objectConverter = null;
                switch (type) {
                    case JSON -> objectConverter = jsonConfig.get(key);
                }
                if (objectConverter != null) {
                    return objectConverter;
                }
            }
        }
        new SimpleLogger("[ezLib]").error("Can not find key '" + key + "'");
        return null;
    }
}
