# ezLib - Your simplified Java library

[![](https://jitpack.io/v/ezTxmMC/ezLib.svg)](https://jitpack.io/#ezTxmMC/ezLib)

## Features

- Easy Configs:
  - Json
  - Properties (WIP)
  - Toml (WIP)
  - Yaml (WIP)
- Easy Databases:
  - MariaDB
  - MongoDB (WIP)
  - SQLite
  - PostgreSQL
  - H2
- Object Converter
- Better JsonObject and JsonArray
- Simplified Logger

## Dependency

### Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.ezTxmMC</groupId>
        <artifactId>ezLib</artifactId>
        <version>Tag</version>
    </dependency>
</dependencies>
```

Get the Tag from https://jitpack.io/#ezTxmMC/ezLib

### Gradle

Groovy:

```groovy
repositories {
    mavenCentral()
    maven {
        url 'https://jitpack.io'
    }
}

dependencies {
    implementation 'com.github.ezTxmMC:ezLib:Tag'
}
```

Kotlin:

```kotlin
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.ezTxmMC:ezLib:Tag")
}
```

Get the Tag from https://jitpack.io/#ezTxmMC/ezLib

## Usage

### Json Config - Create

```java
Config config = new JsonConfig("path", "fileName");
```

This will create a json file in the `path` directory with the `fileName` from the variable.

### Json Config - Set

```java
config.set("key", value);
```

This will set a register with the `key` variable and the `value` variable.

The `key` and `value` variables should be replaced with a unique key and value for your config file.

The `value` variable can be everything, but we recommend the values in Object-Converter.

### Json Config - Remove

```java
config.remove("key");
```

This will remove the register with the `key` variable.

The `key` variable should be replaced with a unique key of your config file.

### Json Config - To JsonObject

```java
((JsonConfig) config).toJsonObject();
```

This will return the built-in better JsonObject.
