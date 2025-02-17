package de.eztxm.ezlib.config;

import de.eztxm.ezlib.api.config.Config;
import de.eztxm.ezlib.config.library.JsonLibrary;
import de.eztxm.ezlib.config.object.JsonObject;
import de.eztxm.ezlib.config.object.ObjectConverter;
import lombok.Getter;
import org.json.JSONObject;  // External dependency for org.json
import com.google.gson.*;

import java.io.*;

/**
 * A standalone configuration class that saves and loads JSON files.
 * Supports three modes: CUSTOM (the built-in implementation), ORG_JSON, and GSON.
 */
public class JsonConfig implements Config {

    // Optional getters:
    @Getter
    private final String configPath;
    @Getter
    private final String configName;
    @Getter
    private final JsonLibrary library;

    // Internal representations (only one will be used depending on the mode)
    private JsonObject customJsonObject;
    private org.json.JSONObject orgJsonObject;
    private com.google.gson.JsonObject gsonObject;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Constructs a new JsonConfig.
     *
     * @param path       Path to the configuration folder.
     * @param configName Name of the configuration file (e.g., "config.json").
     * @param library    The JSON library to use (CUSTOM, ORG_JSON, or GSON).
     */
    public JsonConfig(String path, String configName, JsonLibrary library) {
        this.configPath = path;
        this.configName = configName;
        this.library = library;

        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File configFile = new File(path, configName);
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                // Initialize an empty JSON object based on the chosen library
                switch (library) {
                    case GSON -> gsonObject = new com.google.gson.JsonObject();
                    case ORG_JSON -> orgJsonObject = new JSONObject();
                    case CUSTOM -> customJsonObject = new JsonObject();
                }
                save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        String configJson = readFile(configFile.getAbsolutePath());
        try {
            switch (library) {
                case GSON -> gsonObject = JsonParser.parseString(configJson).getAsJsonObject();
                case ORG_JSON -> orgJsonObject = new JSONObject(configJson);
                case CUSTOM -> customJsonObject = JsonObject.parse(configJson);
            }
        } catch (Exception e) {
            // In case of a parsing error, initialize an empty object.
            switch (library) {
                case GSON -> gsonObject = new com.google.gson.JsonObject();
                case ORG_JSON -> orgJsonObject = new JSONObject();
                case CUSTOM -> customJsonObject = new JsonObject();
            }
        }
    }

    @Override
    public void set(String key, Object value) {
        switch (library) {
            case GSON -> gsonObject.add(key, GSON.toJsonTree(value));
            case ORG_JSON -> orgJsonObject.put(key, value);
            case CUSTOM -> customJsonObject.set(key, value);
        }
        System.out.println("set " + key + " to " + value);
        save();
    }

    @Override
    public void remove(String key) {
        switch (library) {
            case GSON -> gsonObject.remove(key);
            case ORG_JSON -> orgJsonObject.remove(key);
            case CUSTOM -> customJsonObject.remove(key);
        }
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
        return switch (library) {
            case GSON -> {
                JsonElement element = gsonObject.get(key);
                yield (element != null) ? GSON.fromJson(element, Object.class) : null;
            }
            case ORG_JSON -> orgJsonObject.opt(key);
            case CUSTOM -> customJsonObject.get(key);
        };
    }

    public ObjectConverter get(String key) {
        return switch (library) {
            case GSON -> {
                JsonElement element = gsonObject.get(key);
                yield (element != null) ? new ObjectConverter(GSON.fromJson(element, Object.class)) : null;
            }
            case ORG_JSON -> new ObjectConverter(orgJsonObject.opt(key));
            case CUSTOM -> new ObjectConverter(customJsonObject.get(key));
        };
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
        System.out.println("Saving " + configName);
        try (FileWriter writer = new FileWriter(configPath + "/" + configName)) {
            switch (library) {
                case GSON -> writer.write(GSON.toJson(gsonObject));
                case ORG_JSON -> writer.write(orgJsonObject.toString(4));
                case CUSTOM -> {
                    System.out.println(customJsonObject.toJsonString());
                    writer.write(customJsonObject.toJsonString());
                    System.out.println("Saved " + configName);
                }
            }
        } catch (IOException e) {
            System.out.println("SAVE ERROR");
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
