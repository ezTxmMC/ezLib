package de.eztxm.ezlib.config;

import de.eztxm.ezlib.api.config.Config;
import de.eztxm.ezlib.config.object.JsonObject;
import de.eztxm.ezlib.config.object.ObjectConverter;
import lombok.Getter;

import java.io.*;

/**
 * A standalone configuration class that saves and loads JSON files.
 */
@Getter
public class JsonConfig implements Config {
    private final String configPath;
    private final String configName;
    private JsonObject customJsonObject;

    /**
     * Constructs a new JsonConfig.
     *
     * @param path       Path to the configuration folder.
     * @param configName Name of the configuration file (e.g., "config.json").
     */
    public JsonConfig(String path, String configName) {
        this.configPath = path;
        this.configName = configName;

        File folder = new File(path);
        if (!folder.exists()) {
            if (folder.mkdirs()) {
                System.out.println("Created folder " + path);
            }
        }

        File configFile = new File(path, configName);
        if (!configFile.exists()) {
            try {
                if (configFile.createNewFile()) {
                    // Initialize an empty JSON object based on the chosen library
                    customJsonObject = new JsonObject();
                    save();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        String configJson = readFile(configFile.getAbsolutePath());
        try {
            customJsonObject = JsonObject.parse(configJson);
        } catch (Exception e) {
            customJsonObject = new JsonObject();
        }
    }

    @Override
    public void set(String key, Object value) {
        customJsonObject.set(key, value);
        save();
    }

    @Override
    public void remove(String key) {
        customJsonObject.remove(key);
        save();
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
        try (FileWriter writer = new FileWriter(configPath + "/" + configName)) {
            writer.write(customJsonObject.toJsonString(true));
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads the entire content of a file as a String.
     *
     * @param filePath The file path.
     * @return The file content.
     */
    private String readFile(String filePath) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
}
