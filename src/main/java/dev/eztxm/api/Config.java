package dev.eztxm.api;

import dev.eztxm.object.ObjectConverter;

public interface Config {
    void set(String key, Object value);
    void remove(String key);
    ObjectConverter get(String key);
    void addDefault(String key, Object value);
}
