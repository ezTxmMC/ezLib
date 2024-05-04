# ezLib - Your simplified Java library

[![](https://jitpack.io/v/ezTxmMC/ezLib.svg)](https://jitpack.io/#ezTxmMC/ezLib)

## Features

- Easy Configs:
  - Json
  - Toml (WIP)
  - Yaml (WIP)
  - Properties (WIP)
- Easy Databases:
  - H2
  - SQLite
  - MariaDB
  - MongoDB (WIP)
  - PostgreSQL
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
        <version>RELEASE</version>
    </dependency>
</dependencies>
```

Get RELEASE from https://jitpack.io/#ezTxmMC/ezLib

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
    implementation 'com.github.ezTxmMC:ezLib:RELEASE'
}
```

Kotlin:

```kotlin
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.ezTxmMC:ezLib:RELEASE")
}
```

Get RELEASE from https://jitpack.io/#ezTxmMC/ezLib
