package de.eztxm.ezlib.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;

import de.eztxm.ezlib.api.config.Config;
import de.eztxm.ezlib.object.ObjectConverter;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class PropertiesConfig implements Config {
    private final ObjectMapper objectMapper;
    private final File configFile;

    public PropertiesConfig(String path, String configName) {
        File folder = new File(path);
        this.configFile = new File(path + "/" + configName);
        if (!folder.exists()) folder.mkdir();
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                this.objectMapper = new JavaPropsMapper();
                this.objectMapper.writeValue(configFile, new Properties());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            this.objectMapper = new JavaPropsMapper();
        }
    }

    @Override
    public void set(String key, Object value) {
        try {
            Properties properties = this.objectMapper.readValue(configFile, Properties.class);
            properties.setProperty(key, String.valueOf(value));
            this.objectMapper.writeValue(configFile, properties);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(String key) {
        try {
            Properties properties = this.objectMapper.readValue(configFile, Properties.class);
            properties.remove(key);
            this.objectMapper.writeValue(configFile, properties);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ObjectConverter get(String key) {
        try {
            Properties properties = this.objectMapper.readValue(configFile, Properties.class);
            String value = properties.getProperty(key);
            if (value == null) {
                return null;
            }
            return new ObjectConverter(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addDefault(String key, Object value) {
        if (get(key) != null) return;
        set(key, value);
    }
}
