package de.eztxm.ezlib.config;


import de.eztxm.ezlib.config.reflect.JsonProcessor;

public class Test {

    public static void main(String[] args) {
        TestConfig config = new TestConfig();
        JsonProcessor<TestConfig> processor = new JsonProcessor<>(config);
        processor.saveConfiguration();
        config.setAge(18);
        processor.saveConfiguration();
    }
}
