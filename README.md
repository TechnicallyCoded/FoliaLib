# FoliaLib
[![GitHub Release](https://img.shields.io/github/release/technicallycoded/FoliaLib.svg?style=flat)]()

This project is not directly affiliated with Paper team or the Folia project.

<br/>

## Description
This is a wrapper library for aiding in supporting the Folia Paper Fork. This library adds multiple scheduler options to use instead of the Bukkit or Folia native schedulers. Developers are expected to depend on this library and relocate the package to their own to prevent conflicts with other plugins.

Note: This project is still in its early stages and may make frequent breaking changes to the API. Additionally, I won't claim that this library is perfect; If you find any issues, please report them on the [issues page](https://github.com/TechnicallyCoded/FoliaLib/issues)

<br/>

## As a dependency

### Gradle
<details>
  <summary>Click for example code Gradle configurations...</summary>

> [!WARNING]\
> Ensure you relocate FoliaLib using the `relocate` operation in the `shadowJar` task to prevent conflicts with other plugins!

> [!WARNING]\
> Ensure that you exclude FoliaLib from the `minimize` operation!
```groovy
plugins {
    id 'com.github.johnrengelman.shadow' version '8.1.1' // For up to Java 17
    // id 'io.github.goooler.shadow' version '8.1.7' // For Java 21 or higher
}

repositories {
    maven {
        name = "devmart-other"
        url = "https://nexuslite.gcnt.net/repos/other/"
    }
}

dependencies {
    implementation "com.tcoded:FoliaLib:[SEE TOP OF README]"
}

shadowJar {
    // Change the line below to your plugin
    relocate "com.tcoded.folialib", "MY.GROUP.PLUGIN-NAME.lib.folialib"

    // Optional: If you use minimize, make sure you exclude FoliaLib
    minimize {
        exclude dependency("com.tcoded:FoliaLib:.*")
    }
}
```
</details>

### Maven
<details>
  <summary>Click for example code Maven configurations...</summary>

> [!WARNING]\
> You are expected to relocate the package "com.tcoded.folialib" to your own (ex: "me.steve.lib.folialib") to prevent conflicts with other plugins!
```xml
<repositories>
    <repository>
        <id>devmart-other</id>
        <url>https://nexuslite.gcnt.net/repos/other/</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.tcoded</groupId>
        <artifactId>FoliaLib</artifactId>
        <version>[SEE TOP OF README]</version>
        <scope>compile</scope>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>3.5.0</version>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <relocations>
                    <relocation>
                        <pattern>com.tcoded.folialib</pattern>
                        <!-- !! MAKE SURE TO CHANGE THIS TO YOUR PLUGIN !! -->
                        <shadedPattern>MY.GROUP.PLUGIN-NAME.lib.folialib</shadedPattern>
                    </relocation>
                </relocations>
            </configuration>
        </plugin>
    </plugins>
</build>
```
</details>

<br/>

## How to use
Create a new instance of the FoliaLib class:
```java
// Assuming "this" is your plugin instance
FoliaLib foliaLib = new FoliaLib(this);
```
Here are some examples of how to use the library:
```java
// Remember that 20 ticks = 1000 milliseconds, and 20 * 50L = 1000L

// On a folia server, runNextTick() will run using the GlobalRegionScheduler
//   !! This does not make it safe to use for players or block changes !!
//   Use other methods below for handling those
//   This should only be used for updating the following (see GlobalRegionScheduler.java for more info)
//     - world day time, world game time, weather cycle, sleep night skipping, executing commands for console, and other misc
// On a Spigot server, runNextTick() will run on the main thread
foliaLib.getImpl().runNextTick(() -> {/* Code */});

// In both cases, this method will run asynchronously
foliaLib.getImpl().runAsync(() -> {/* Code */});

// On a Folia server, this method will run using the GlobalRegionScheduler
// On a Spigot server, this method will run on the main thread
foliaLib.getImpl().runTimer(() -> {/* Code */}, 1, 20); // Using ticks
foliaLib.getImpl().runTimer(() -> {/* Code */}, 1, 20 * 50L, TimeUnit.MILLISECONDS); // Using TimeUnit

// In both cases, this method will run asynchronously
foliaLib.getImpl().runTimerAsync(() -> {/* Code */}, 1, 20); // Using ticks
foliaLib.getImpl().runTimerAsync(() -> {/* Code */}, 1, 20 * 50L, TimeUnit.MILLISECONDS); // Using TimeUnit

// On a folia server, this will run the code using the RegionScheduler that is appropriate for the location
// On a spigot server, this will run the code on the main thread
foliaLib.getImpl().runAtLocation(location, () -> {/* Code */});

// On a folia server, this will run the code using the EntityScheduler that is appropriate for the entity
// On a spigot server, this will just run the code on the main thread
foliaLib.getImpl().runAtEntity(entity, () -> {/* Code */});

// AND MANY MORE OPTIONS...!
```

<br/>

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
