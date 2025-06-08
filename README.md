# FoliaLib &nbsp; &nbsp; [![GitHub Release](https://img.shields.io/github/release/technicallycoded/FoliaLib.svg?style=flat)]()

Developers and contributors like you make this project possible. This project is not directly affiliated with PaperMC. 

<br/>

## Description
This project is a wrapper library that aids developers in supporting the "Folia" Paper fork while not breaking support for Spigot or Paper. This library adds multiple scheduler options to use instead of the Bukkit or Folia native schedulers. Developers are expected to depend on this library and relocate the package to their own to prevent conflicts with other plugins.

**Version support**:  
FoliaLib attempts to be compatible with all your projects no matter how outdated the minecraft version. As of writing, FoliaLib should work in any plugin supporting 1.8.8 to 1.21+. However, we *heavily* warn against using anything below 1.13.2 since this version adds support for many features FoliaLib depends on. Workarounds are used for supporting older versions.

**API Note**:  
This project is still in its early stages and may make frequent breaking changes to the API. Additionally, I won't claim that this library is perfect; If you find any issues, please report them on the [issues page](https://github.com/TechnicallyCoded/FoliaLib/issues)

<br/>

## FoliaLib as a dependency

> [!CAUTION]
> The FoliaLib repository has moved! Please update your `pom.xml` or `build.gradle` file to use the Jitpack repository (see examples below).

> [!WARNING]
> You are expected to relocate the package `com.tcoded.folialib` to prevent conflicts with other plugins!  
> Example configuration snippets demonstrating how to do this are available below for Maven and Gradle.

> [!TIP]
> Ensure that you exclude FoliaLib from any `minimize` operation.

### Gradle Dependency
<details>
  <summary>[Click to show Gradle example configurations]</summary>

```groovy
plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1" // For up to Java 17
    // id("com.gradleup.shadow") version "9.0.0-beta12" // Uncomment for Java 21 or higher
}

repositories {
    maven {
        name = "jitpack"
        url = "https://jitpack.io"
    }
}

dependencies {
    implementation("com.github.technicallycoded:FoliaLib:main-SNAPSHOT")
}

shadowJar {
    // !! MAKE SURE TO CHANGE THIS TO YOUR PLUGIN'S GROUP & PLUGIN NAME !!
    relocate("com.tcoded.folialib", "CHANGE-THE-GROUP.CHANGE-THE-PLUGIN-NAME.lib.folialib")

    // Optional: If you use minimize, make sure you exclude FoliaLib
    // Do not uncomment this if you don't know what you are doing
    // minimize {
    //     exclude dependency("com.github.technicallycoded:FoliaLib:.*")
    // }
}
```
</details>

### Maven Dependency
<details>
  <summary>[Click to show Maven example configurations]</summary>

```xml
<repositories>
    <repository>
        <id>jitpack</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.technicallycoded</groupId>
        <artifactId>FoliaLib</artifactId>
        <version>main-SNAPSHOT</version>
        <scope>compile</scope>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>3.6.0</version>
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
                        <!-- !! MAKE SURE TO CHANGE THIS TO YOUR PLUGIN'S GROUP & PLUGIN NAME !! -->
                        <shadedPattern>CHANGE-THE-GROUP.CHANGE-THE-PLUGIN-NAME.lib.folialib</shadedPattern>
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
//   Use other methods found below for handling these cases
//   This should only be used for updating the following (see GlobalRegionScheduler.java for more info)
//     - world day time
//     - world game time
//     - weather cycle
//     - sleep/night skipping
//     - executing commands for console
// On a Spigot server, runNextTick() will run on the main thread
foliaLib.getScheduler().runNextTick(() -> {/* Code */});

// In both cases, this method will run asynchronously
foliaLib.getScheduler().runAsync(() -> {/* Code */});

// On a Folia server, this method will run using the GlobalRegionScheduler
// On a Spigot server, this method will run on the main thread
foliaLib.getScheduler().runTimer(() -> {/* Code */}, 1, 20); // Using ticks

// In both cases, this method will run asynchronously
foliaLib.getScheduler().runTimerAsync(() -> {/* Code */}, 1, 20); // Using ticks
foliaLib.getScheduler().runTimerAsync(() -> {/* Code */}, 1, 20 * 50L, TimeUnit.MILLISECONDS); // Using TimeUnit

// On a folia server, this will run the code using the RegionScheduler that is appropriate for the location
// On a spigot server, this will run the code on the main thread
foliaLib.getScheduler().runAtLocation(location, () -> {/* Code */});

// On a folia server, this will run the code using the EntityScheduler that is appropriate for the entity
// On a spigot server, this will just run the code on the main thread
foliaLib.getScheduler().runAtEntity(entity, () -> {/* Code */});

// On a folia server, this will teleport the entity (or player) asynchronously
// On a paper server (which supports async teleports), this will also teleport asynchronously
// On a spigot server, this will fallback to teleporting the player on the next tick
//   Why not immediately teleport on spigot?
//     1. We don't know if you're running this from the main thread (but this could be checked)
//     2. Above all, we quickly expose any bugs that could result from expecting instant
//        teleports on any platform
// Returns: A CompletableFuture<Boolean> from which you can execute any dependent code. The Boolean
//          is the success state of the teleportation.
foliaLib.getScheduler().teleportAsync(entity, location, optionalTeleportCause);

// During your plugin's onDisable(), please use the following.
// This will cancel all the tasks associated with this FoliaLib instance
foliaLib.getScheduler().cancelAllTasks();

// AND MANY MORE OPTIONS...!

// You can also manually check for a specific platform if certain parts of your code
// should only run under those conditions (ex: use of specific folia-only APIs)
foliaLib.isFolia(); // true/false
foliaLib.isPaper(); // true/false
foliaLib.isSpigot(); // true/false
```

<br/>

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
