package de.eztxm.api.config;

import de.eztxm.object.ObjectConverter;

public interface Config {
    void set(String key, Object value);
    void remove(String key);
    ObjectConverter get(String key);
    void addDefault(String key, Object value);
}
