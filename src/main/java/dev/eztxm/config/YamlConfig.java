package dev.eztxm.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import dev.eztxm.api.Config;
import dev.eztxm.object.ObjectConverter;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class YamlConfig implements Config {
    private final YAMLMapper yamlMapper;
    private final File configFile;

    public YamlConfig(String path, String configName) {
        File folder = new File(path);
        this.configFile = new File(path + "/" + configName + ".yml");
        if (!folder.exists()) folder.mkdir();
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                this.yamlMapper = new YAMLMapper();
                this.yamlMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            this.yamlMapper = new YAMLMapper();
        }
    }

    @Override
    public void set(String key, Object value) {
        try {
            Object obj = this.yamlMapper.readValue(configFile, Object.class);
            ((Map<String, Object>) obj).put(key, value);
            this.yamlMapper.writeValue(configFile, obj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(String key) {
        try {
            Object obj = this.yamlMapper.readValue(configFile, Object.class);
            ((Map<String, Object>) obj).remove(key);
            this.yamlMapper.writeValue(configFile, obj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ObjectConverter get(String key) {
        try {
            Object obj = this.yamlMapper.readValue(configFile, Object.class);
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

    @Override
    public void save() {}
}
