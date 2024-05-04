# ezLib - Your simplified Java library

![GitHub Release](https://img.shields.io/github/v/release/ezTxmMC/ezLib?include_prereleases&style=for-the-badge&color=%23d97325)
![GitHub milestone details](https://img.shields.io/github/milestones/progress-percent/ezTxmMC/ezLib/1?style=for-the-badge)
![GitHub watchers](https://img.shields.io/github/watchers/ezTxmMC/ezLib?style=for-the-badge)
![GitHub Repo stars](https://img.shields.io/github/stars/ezTxmMC/ezLib?style=for-the-badge)
![GitHub forks](https://img.shields.io/github/forks/ezTxmMC/ezLib?style=for-the-badge)

## Features

- Easy Configs:
  - XML (WIP)
  - Json
  - Toml
  - YAML
  - Properties
- Annotation Configs (WIP)
- Easy Databases:
  - H2
  - Redis (WIP)
  - SQLite
  - MariaDB
  - MongoDB (WIP)
  - InfluxDB (WIP)
  - PostgreSQL
  - DragonflyDB (WIP)
- Annotation Databases (WIP)
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
        <version>VERSION</version>
    </dependency>
</dependencies>
```

Get VERSION from https://jitpack.io/#ezTxmMC/ezLib

### Gradle

Groovy:

```groovy
repositories {
    maven {
        url 'https://jitpack.io'
    }
}

dependencies {
    implementation 'com.github.ezTxmMC:ezLib:VERSION'
}
```

Kotlin:

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.ezTxmMC:ezLib:VERSION")
}
```

Get VERSION from https://jitpack.io/#ezTxmMC/ezLib
