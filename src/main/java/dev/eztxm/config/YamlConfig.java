package dev.eztxm.config;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import dev.eztxm.config.util.Config;
import dev.eztxm.object.ObjectConverter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class YamlConfig implements Config {

    private final Path configPath;
    private final Yaml yaml;
    private Map<String, Object> data;

    public YamlConfig(String filePath) {
        this.configPath = Paths.get(filePath);
        this.yaml = new Yaml(new Constructor(new LoaderOptions()));
        if (Files.exists(configPath)) {
            try {
                this.data = yaml.load(Files.newInputStream(configPath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
             return;
         }
         this.data = new HashMap<>();
    }

    @Override
    public void set(String key, Object value) {
        this.data.put(key, value);
        this.save();
    }

    @Override
    public void remove(String key) {
        // todo
    }

    @Override
    public ObjectConverter get(String key) {
        return new ObjectConverter(this.data.get(key));
    }

    @Override
    public void save() {
        try {
            String output = yaml.dump(this.data);
            Files.write(configPath, output.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
