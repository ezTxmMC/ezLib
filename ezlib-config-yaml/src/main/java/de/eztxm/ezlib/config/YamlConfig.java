package de.eztxm.ezlib.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import de.eztxm.ezlib.api.config.Config;
import de.eztxm.ezlib.object.ObjectConverter;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class YamlConfig implements Config {
    private final YAMLMapper yamlMapper;
    private final File configFile;

    public YamlConfig(String path, String configName) {
        File folder = new File(path);
        this.configFile = new File(path + "/" + configName);
        if (!folder.exists()) folder.mkdir();
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                this.yamlMapper = new YAMLMapper();
                this.yamlMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        this.yamlMapper = new YAMLMapper();
    }

    @Override
    public void set(String key, Object value) {
        try {
            Object map = this.yamlMapper.readValue(configFile, Object.class);
            ((Map<String, Object>) map).put(key, value);
            this.yamlMapper.writeValue(configFile, map);
        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }

    @Override
    public void remove(String key) {
        try {
            Object obj = this.yamlMapper.readValue(configFile, Object.class);
            ((Map<String, Object>) obj).remove(key);
            this.yamlMapper.writeValue(configFile, obj);
        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }

    @Override
    public ObjectConverter getObject(String key) {
        try {
            Object obj = this.yamlMapper.readValue(configFile, Object.class);
            Object value = ((Map<String, Object>) obj).get(key);
            return new ObjectConverter(value);
        } catch (IOException e) {
            return null;
        }
    }

    public Object getAsObject(String key) {
        try {
            Object obj = this.yamlMapper.readValue(configFile, Object.class);
            return ((Map<String, Object>) obj).get(key);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void addDefault(String key, Object value) {
        if (getObject(key) != null) return;
        set(key, value);
    }
}
