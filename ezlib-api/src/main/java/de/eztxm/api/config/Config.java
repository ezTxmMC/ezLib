package de.eztxm.api.config;

public interface Config {
    void set(String key, Object value);
    void remove(String key);
    Object get(String key);
    void addDefault(String key, Object value);
}
