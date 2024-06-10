# ezLib - Your simplified Java library

![GitHub Release](https://img.shields.io/github/v/release/ezTxmMC/ezLib?include_prereleases&style=for-the-badge&color=%23d97325)
![GitHub milestone details](https://img.shields.io/github/milestones/progress-percent/ezTxmMC/ezLib/2?style=for-the-badge)
![GitHub watchers](https://img.shields.io/github/watchers/ezTxmMC/ezLib?style=for-the-badge)
![GitHub Repo stars](https://img.shields.io/github/stars/ezTxmMC/ezLib?style=for-the-badge)
![GitHub forks](https://img.shields.io/github/forks/ezTxmMC/ezLib?style=for-the-badge)

## Features

- Easy Configs:
  - XML (WIP)
  - Json
  - TOML
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
- Custom Terminal
- Object Converter
- Better JsonObject and JsonArray
- Simplified Logger

## Dependency

### Maven

```xml
<repositories>
    <repository>
        <id>eztxm-repo</id>
        <url>https://repo.eztxm.de/maven/</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>de.eztxm</groupId>
        <artifactId>ezlib-all</artifactId>
        <version>1.0-ALPHA10</version>
    </dependency>
</dependencies>
```

``eztxm-all`` can be replaced by any module name.

### Gradle

Groovy:

```groovy
repositories {
    maven {
        url 'https://repo.eztxm.de/maven/'
    }
}

dependencies {
    implementation 'de.eztxm:ezlib-all:1.0-ALPHA10'
}
```

Kotlin:

```kotlin
repositories {
    maven("https://repo.eztxm.de/maven/")
}

dependencies {
    implementation("de.eztxm:ezlib-all:1.0-ALPHA10")
}
```

``eztxm-all`` can be replaced by any module name.

## Todo

- Serial by @DragonRex004
- GSON-Handler by @ezTxmMC
- Redis/DragonflyDB by @ezTxmMC
- InfluxDB by @ezTxmMC
- XML Configs by @ezTxmMC
- Expand Terminal and add integraded multi-terminal system by @ezTxmMC & @DragonRex004
