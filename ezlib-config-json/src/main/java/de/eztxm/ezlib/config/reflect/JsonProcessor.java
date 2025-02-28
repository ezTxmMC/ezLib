package de.eztxm.ezlib.config.reflect;

import de.eztxm.ezlib.config.JsonConfig;
import de.eztxm.ezlib.config.annotation.JsonConfiguration;
import de.eztxm.ezlib.config.annotation.JsonElement;
import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Field;

@Getter
public class JsonProcessor<T> {
    private final JsonConfig config;

    @SneakyThrows
    public JsonProcessor(T instance) {
        JsonConfiguration configuration = instance.getClass().getAnnotation(JsonConfiguration.class);
        this.config = new JsonConfig(configuration.path(), configuration.fileName(), false);
        for (Field declaredField : instance.getClass().getDeclaredFields()) {
            if (!declaredField.isAnnotationPresent(JsonElement.class)) {
                continue;
            }
            declaredField.setAccessible(true);
            Object value = declaredField.get(instance);
            config.set(declaredField.getName(), value);
        }
    }

    public void saveConfiguration() {
        this.config.save();
    }
}
