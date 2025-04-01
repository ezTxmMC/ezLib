package de.eztxm.ezlib.config.reflect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import de.eztxm.ezlib.config.annotation.JsonClassConfig;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;

@Getter
public class JsonProcessor<T> {

    private final T instance;

    public JsonProcessor(T instance) {
        this.instance = instance;
        if (!instance.getClass().isAnnotationPresent(JsonClassConfig.class)) {
            throw new IllegalStateException(
                    "Klasse " + instance.getClass().getName() + " hat keine JsonClassConfig-Annotation!");
        }
    }

    @SneakyThrows
    public void saveConfiguration() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();

        JsonClassConfig configuration = instance.getClass().getAnnotation(JsonClassConfig.class);
        File file = new File(configuration.path(), configuration.fileName());
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        writer.writeValue(file, instance);
    }

    @SneakyThrows
    public static <T> JsonProcessor<T> loadConfiguration(Class<T> clazz) {
        if (!clazz.isAnnotationPresent(JsonClassConfig.class)) {
            throw new IllegalStateException(
                    "Klasse " + clazz.getName() + " hat keine JsonClassConfig-Annotation!");
        }

        JsonClassConfig configuration = clazz.getAnnotation(JsonClassConfig.class);
        File file = new File(configuration.path(), configuration.fileName());

        ObjectMapper objectMapper = new ObjectMapper();

        T instance;
        if (file.exists() && file.length() > 0) {
            instance = objectMapper.readValue(file, clazz);
        } else {
            instance = clazz.getDeclaredConstructor().newInstance();
        }

        return new JsonProcessor<>(instance);
    }
}
