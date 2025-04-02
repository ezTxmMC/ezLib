package de.eztxm.ezlib.config;

import de.eztxm.ezlib.api.config.Config;
import de.eztxm.ezlib.config.mapper.JsonMapper;
import de.eztxm.ezlib.config.object.JsonObject;
import de.eztxm.ezlib.config.object.ObjectConverter;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Getter
public class JsonConfig implements Config {
    private final Path configFolder;
    private final String configName;
    private final boolean autoSave;
    private JsonObject json;

    public JsonConfig(String path, String configName) {
        this(path, configName, true);
    }

    public JsonConfig(String path, String configName, boolean autoSave) {
        this.configFolder = Paths.get(path);
        this.configName = configName;
        this.autoSave = autoSave;

        try {
            if (Files.notExists(configFolder)) {
                Files.createDirectories(configFolder);
            }

            Path file = configFolder.resolve(configName);

            if (Files.notExists(file)) {
                Files.createFile(file);
                this.json = new JsonObject();
                if (autoSave) {
                    save();
                }
                return;
            }

            String raw = Files.readString(file).trim();
            if (raw.isEmpty()) {
                this.json = new JsonObject();
                return;
            }

            try {
                this.json = JsonObject.parse(raw);
            } catch (Exception e) {
                this.json = new JsonObject();
            }
        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Laden der Konfiguration: " + e.getMessage(), e);
        }
    }

    @Override
    public void set(String key, Object value) {
        json.put(key, value);
        if (autoSave) {
            save();
        }
    }

    @Override
    public void remove(String key) {
        json.remove(key);
        if (autoSave) {
            save();
        }
    }

    @Override
    public Object getObject(String key) {
        return json.get(key);
    }

    public ObjectConverter get(String key) {
        return new ObjectConverter(json.get(key));
    }

    @Override
    public void addDefault(String key, Object value) {
        if (json.containsKey(key)) {
            return;
        }
        set(key, value);
    }

    public void save() {
        Path file = configFolder.resolve(configName);
        try {
            Files.writeString(file, json.toJsonString());
        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Speichern der Konfiguration: " + e.getMessage(), e);
        }
    }

    public <T> T mapTo(Class<T> clazz) {
        return JsonMapper.fromJson(json.toJsonString(), clazz);
    }

    public <T> void mapFrom(T obj) {
        this.json = JsonObject.parse(JsonMapper.toJson(obj));
        if (autoSave) {
            save();
        }
    }
}
