package de.eztxm.ezlib.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.toml.TomlMapper;
import de.eztxm.ezlib.api.config.Config;
import de.eztxm.ezlib.object.ObjectConverter;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class TomlConfig implements Config {
    private final TomlMapper tomlMapper;
    private final File configFile;

    public TomlConfig(String path, String configName) {
        File folder = new File(path);
        this.configFile = new File(path + "/" + configName);
        if (!folder.exists()) folder.mkdir();
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                this.tomlMapper = new TomlMapper();
                this.tomlMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            this.tomlMapper = new TomlMapper();
        }
    }

    @Override
    public void set(String key, Object value) {
        try {
            Object obj = this.tomlMapper.readValue(configFile, Object.class);
            ((Map<String, Object>) obj).put(key, value);
            this.tomlMapper.writeValue(configFile, obj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(String key) {
        try {
            Object obj = this.tomlMapper.readValue(configFile, Object.class);
            ((Map<String, Object>) obj).remove(key);
            this.tomlMapper.writeValue(configFile, obj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ObjectConverter get(String key) {
        try {
            Object obj = this.tomlMapper.readValue(configFile, Object.class);
            Object value = ((Map<String, Object>) obj).get(key);
            if (value == null) {
                return null;
            }
            return new ObjectConverter(value);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void addDefault(String key, Object value) {
        if (get(key) != null) return;
        set(key, value);
    }
}