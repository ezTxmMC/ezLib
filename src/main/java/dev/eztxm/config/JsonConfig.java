package dev.eztxm.config;

import dev.eztxm.config.util.ObjectFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;


public class JsonConfig {
    private final String configPath;
    private final String configName;
    private final JSONObject config;

    public JsonConfig(String path, String configName) {
        this.configPath = path;
        this.configName = configName + ".json";
        File folder = new File(path);
        File configFile = new File(path + "/" + this.configName);
        if (!folder.exists()) folder.mkdir();
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                this.config = new JSONObject();
                saveConfig();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            String configJson = readFile(path + "/" + this.configName);
            this.config = new JSONObject(configJson);
        }
    }

    public void set(String key, Object value) {
        this.config.put(key, value);
        saveConfig();
    }

    public ObjectFormatter get(String key) {
        try {
            Object object = this.config.get(key);
            return new ObjectFormatter(object);
        } catch (JSONException e) {
            return null;
        }
    }

    public void remove(String key) {
        this.config.remove(key);
        saveConfig();
    }

    public JSONObject toJSONObject() {
        return config;
    }

    private void saveConfig() {
        try (FileWriter file = new FileWriter(configPath + "/" + configName)) {
            file.write(this.config.toString());
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
