package dev.eztxm.config;

import dev.eztxm.object.JsonObject;
import dev.eztxm.object.ObjectFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;


public class JsonConfig {
    private final String configPath;
    private final String configName;
    private final JSONObject jsonObject;

    public JsonConfig(String path, String configName) {
        this.configPath = path;
        this.configName = configName + ".json";
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
        } else {
            String configJson = readFile(path + "/" + this.configName);
            this.jsonObject = new JSONObject(configJson);
        }
    }

    public void set(String key, Object value) {
        this.jsonObject.put(key, value);
        save();
    }

    public ObjectFormatter get(String key) {
        try {
            Object object = this.jsonObject.get(key);
            return new ObjectFormatter(object);
        } catch (JSONException e) {
            return null;
        }
    }

    public void remove(String key) {
        this.jsonObject.remove(key);
        save();
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
