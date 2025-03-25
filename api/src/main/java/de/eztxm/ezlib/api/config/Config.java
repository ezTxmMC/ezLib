package de.eztxm.ezlib.api.config;

public interface Config {
    void set(String key, Object value);
    void remove(String key);
    Object getObject(String key);
    void addDefault(String key, Object value);
}
