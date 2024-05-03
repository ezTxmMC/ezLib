package dev.eztxm.config;

import dev.eztxm.config.util.Config;
import dev.eztxm.object.ObjectConverter;

public class TomlConfig implements Config {

    @Override
    public void set(String key, Object value) {
        // todo
    }

    @Override
    public void remove(String key) {
        // todo
    }

    @Override
    public ObjectConverter get(String key) {
        return new ObjectConverter(key); // todo
    }

    @Override
    public void addDefault(String key, Object value) {
        if (get(key) != null) return;
        set(key, value);
    }

    @Override
    public void save() {
        // todo
    }
}
