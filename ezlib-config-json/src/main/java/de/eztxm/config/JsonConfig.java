package de.eztxm.config;

import de.eztxm.api.config.Config;
import de.eztxm.config.object.JsonObject;
import de.eztxm.config.object.ObjectConverter;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

public class JsonConfig implements Config {
    private final String configPath;
    private final String configName;
    private final JSONObject jsonObject;


    /**
     * Creates a config file.
     * @param path - The folder path where the config should be.
     * @param configName - The Name of the config without the type-extension.
     */
    public JsonConfig(String path, String configName) {
        this.configPath = path;
        this.configName = configName;
        File folder = new File(path);
        File configFile = new File(path + "/" + this.configName);
        if (!folder.exists()) folder.mkdir();
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                this.jsonObject = new JSONObject();
                save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        String configJson = readFile(path + "/" + this.configName);
        this.jsonObject = new JSONObject(configJson);
    }

    @Override
    public void set(String key, Object value) {
        this.jsonObject.put(key, value);
        save();
    }

    @Override
    public void remove(String key) {
        this.jsonObject.remove(key);
        save();
    }

    @Override
    public ObjectConverter get(String key) {
        try {
            Object object = this.jsonObject.get(key);
            return new ObjectConverter(object);
        } catch (JSONException e) {
            return null;
        }
    }

    @Override
    public void addDefault(String key, Object value) {
        if (get(key) != null) return;
        set(key, value);
    }

    public JsonObject toJsonObject() {
        return new JsonObject(this, jsonObject);
    }

    public void save() {
        try (FileWriter file = new FileWriter(configPath + "/" + configName)) {
            file.write(this.jsonObject.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String readFile(String filePath) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stringBuilder.toString();
    }
}
