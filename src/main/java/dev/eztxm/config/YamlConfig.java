package dev.eztxm.config;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class YamlConfig {

    private final Path configPath;
    private final Yaml yaml;
    private Map<String, Object> data;

    public YamlConfig(String filePath) {
        this.configPath = Paths.get(filePath);
        this.yaml = new Yaml(new Constructor(new LoaderOptions()));
        this.loadConfig();
    }

    private void loadConfig() {
        if (Files.exists(configPath)) {
           try {
               this.data = yaml.load(Files.newInputStream(configPath));
           } catch (IOException e) {
               // Failed to load
               throw new RuntimeException("");
           }
            return;
        }
        this.data = new HashMap<>();
    }

    private void saveConfig() {
        try {
            String output = yaml.dump(this.data);
            Files.write(configPath, output.getBytes());
        } catch (IOException e) {
            // Failed to save
            throw new RuntimeException("");
        }
    }

    public void set(String key, Object value) {
        this.data.put(key, value);
        this.saveConfig();
    }

    public Object get(String key) {
        return this.data.get(key);
    }


}
