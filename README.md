# ezLib - Your simplified Java library

![GitHub Release](https://img.shields.io/github/v/release/ezTxmMC/ezLib?include_prereleases&style=for-the-badge&color=%23d97325)
![GitHub milestone details](https://img.shields.io/github/milestones/progress-percent/ezTxmMC/ezLib/4?style=for-the-badge)
![GitHub watchers](https://img.shields.io/github/watchers/ezTxmMC/ezLib?style=for-the-badge)
![GitHub Repo stars](https://img.shields.io/github/stars/ezTxmMC/ezLib?style=for-the-badge)
![GitHub forks](https://img.shields.io/github/forks/ezTxmMC/ezLib?style=for-the-badge)

## Features

- Easy Configs:
  - Json
  - TOML
  - YAML
  - Properties
- Easy Databases:
  - SQLite
  - MariaDB
  - MongoDB
- Object Converter
- Own JsonObject and JsonArray
- JsonClassConfig and JsonClassElement Annotation for class configuration

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
        <version>1.0-ALPHA11</version>
    </dependency>
</dependencies>
```

``ezlib-all`` can be replaced by any module name.

### Gradle

Groovy:

```groovy
repositories {
    maven {
        url 'https://repo.eztxm.de/maven/'
    }
}

dependencies {
    implementation 'de.eztxm:ezlib-all:1.0-ALPHA11'
}
```

Kotlin:

```kotlin
repositories {
    maven("https://repo.eztxm.de/maven/")
}

dependencies {
    implementation("de.eztxm:ezlib-all:1.0-ALPHA11")
}
```

``ezlib-all`` can be replaced by any module name.

## Dependency Snapshots

### Maven

```xml
<repositories>
    <repository>
        <id>eztxm-repo</id>
        <url>https://repo.eztxm.de/maven/snapshots/</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>de.eztxm</groupId>
        <artifactId>ezlib</artifactId>
        <version>1.0-BETA1.indev6</version>
    </dependency>
</dependencies>
```

``ezlib`` can be replaced by any module name, be careful if you use `json` you must change the groupId to `de.eztxm.ezlib.config`.

### Gradle

Groovy:

```groovy
repositories {
    maven {
        url 'https://repo.eztxm.de/maven/snapshots/'
    }
}

dependencies {
    implementation 'de.eztxm:ezlib:1.0-BETA1.indev6'
}
```

Kotlin:

```kotlin
repositories {
    maven("https://repo.eztxm.de/maven/snapshots/")
}

dependencies {
    implementation("de.eztxm:ezlib:1.0-BETA1.indev6")
}
```

``ezlib`` can be replaced by any module name, be careful if you use `json` you must change the groupId to `de.eztxm.ezlib.config`.

## TODO

- Converter for org.json and google gson to ezlib json and back
- Database annotations
