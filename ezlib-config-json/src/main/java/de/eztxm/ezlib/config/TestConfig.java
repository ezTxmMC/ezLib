package de.eztxm.ezlib.config;

import de.eztxm.ezlib.config.annotation.JsonConfiguration;
import de.eztxm.ezlib.config.annotation.JsonElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonConfiguration
public class TestConfig {
    @JsonElement
    private String name;
    @JsonElement
    private int age;
}
