package de.eztxm.ezlib.config;

import de.eztxm.ezlib.api.config.Config;
import de.eztxm.ezlib.config.object.JsonObject;
import de.eztxm.ezlib.config.object.ObjectConverter;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A standalone configuration class that saves and loads JSON files.
 */
@Getter
public class JsonConfig implements Config {
    private final Path configFolder;
    private String configName;
    private boolean autoSave;
    private JsonObject customJsonObject;

    /**
     * Constructs a new JsonConfig.
     *
     * @param path       Path to the configuration folder.
     * @param configName Name of the configuration file (e.g., "config.json").
     */
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
            Path configFile = configFolder.resolve(configName);
            if (Files.notExists(configFile)) {
                Files.createFile(configFile);
                customJsonObject = new JsonObject();
                if (autoSave) {
                    save();
                }
                return;
            }
            String configJson = Files.readString(configFile);
            if (configJson.trim().isEmpty()) {
                customJsonObject = new JsonObject();
                return;
            }
            try {
                customJsonObject = JsonObject.parse(configJson);
            } catch (Exception e) {
                customJsonObject = new JsonObject();
            }
        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Initialisieren der Konfiguration: " + e.getMessage(), e);
        }
    }

    @Override
    public void set(String key, Object value) {
        customJsonObject.set(key, value);
        if (autoSave) {
            save();
        }
    }

    @Override
    public void remove(String key) {
        customJsonObject.remove(key);
        if (autoSave) {
            save();
        }
    }

    /**
     * Returns the raw object for the given key.
     *
     * @param key the key.
     * @return the underlying object.
     */
    @Override
    public Object getObject(String key) {
        return customJsonObject.get(key);
    }

    public ObjectConverter get(String key) {
        return new ObjectConverter(customJsonObject.get(key));
    }

    @Override
    public void addDefault(String key, Object value) {
        if (getObject(key) != null) return;
        set(key, value);
    }

    /**
     * Saves the current JSON configuration to the file.
     */
    public void save() {
        Path configFile = configFolder.resolve(configName);
        try {
            Files.writeString(configFile, customJsonObject.toJsonString(true));
        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Speichern der Konfiguration: " + e.getMessage(), e);
        }
    }
}
