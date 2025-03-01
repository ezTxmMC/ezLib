package de.eztxm.ezlib.config.reflect;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.eztxm.ezlib.config.JsonConfig;
import de.eztxm.ezlib.config.annotation.JsonClassConfig;
import de.eztxm.ezlib.config.annotation.JsonClassElement;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;
import java.lang.reflect.Field;

@Getter
public class JsonProcessor<T> {
    private final T instance;
    private final JsonConfig config;

    public JsonProcessor(T instance) {
        this.instance = instance;
        JsonClassConfig configuration = instance.getClass().getAnnotation(JsonClassConfig.class);
        this.config = new JsonConfig(configuration.path(), configuration.fileName(), false);
    }

    @SneakyThrows
    private void process() {
        for (Field declaredField : instance.getClass().getDeclaredFields()) {
            if (!declaredField.isAnnotationPresent(JsonClassElement.class)) {
                continue;
            }
            declaredField.setAccessible(true);
            Object value = declaredField.get(instance);
            config.set(declaredField.getName(), value);
        }
    }

    public void saveConfiguration() {
        this.process();
        this.config.save();
    }

    @SneakyThrows
    public static <T> JsonProcessor<T> loadConfiguration(Class<T> clazz) {
        JsonClassConfig configuration = clazz.getAnnotation(JsonClassConfig.class);
        if (configuration == null) {
            return null;
        }
        File file = new File(configuration.path() + "/" + configuration.fileName());
        ObjectMapper objectMapper = new ObjectMapper();
        T instance;
        if (file.exists()) {
            instance = objectMapper.readValue(file, clazz);
        } else {
            instance = clazz.getDeclaredConstructor().newInstance();
        }
        return new JsonProcessor<>(instance);
    }
}
