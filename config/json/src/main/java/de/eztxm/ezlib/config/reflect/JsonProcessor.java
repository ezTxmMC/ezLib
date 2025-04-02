package de.eztxm.ezlib.config.reflect;

import de.eztxm.ezlib.config.annotation.JsonConfig;
import de.eztxm.ezlib.config.mapper.JsonMapper;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Getter
public class JsonProcessor<T> {

    private final T instance;
    private final Path file;

    public JsonProcessor(T instance) {
        this.instance = instance;

        Class<?> clazz = instance.getClass();
        if (!clazz.isAnnotationPresent(JsonConfig.class)) {
            throw new IllegalStateException("Klasse " + clazz.getName() + " hat keine JsonClassConfig-Annotation!");
        }

        JsonConfig annotation = clazz.getAnnotation(JsonConfig.class);
        this.file = Paths.get(annotation.path(), annotation.fileName());
    }

    public void saveConfiguration() {
        try {
            if (Files.notExists(file.getParent())) {
                Files.createDirectories(file.getParent());
            }
            Files.writeString(file, JsonMapper.toJson(instance));
        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Speichern der Konfiguration: " + e.getMessage(), e);
        }
    }

    public static <T> JsonProcessor<T> loadConfiguration(Class<T> clazz) {
        if (!clazz.isAnnotationPresent(JsonConfig.class)) {
            throw new IllegalStateException("Klasse " + clazz.getName() + " hat keine JsonClassConfig-Annotation!");
        }

        JsonConfig annotation = clazz.getAnnotation(JsonConfig.class);
        Path file = Paths.get(annotation.path(), annotation.fileName());

        T instance;
        try {
            if (Files.exists(file) && Files.size(file) > 0) {
                String raw = Files.readString(file);
                instance = JsonMapper.fromJson(raw, clazz);
            } else {
                instance = clazz.getDeclaredConstructor().newInstance();
            }
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Laden der Konfiguration: " + e.getMessage(), e);
        }

        return new JsonProcessor<>(instance);
    }
}