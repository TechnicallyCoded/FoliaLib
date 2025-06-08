plugins {
    id("maven-publish")
    id("java")
}

group = "com.tcoded"

allprojects {
    apply(plugin = "java")

    version = "0.5.0"

    repositories {
        mavenCentral()

        maven {
            url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        }

        maven {
            url = uri("https://hub.spigotmc.org/nexus/content/repositories/public/")
        }

        maven {
            url = uri("https://repo.papermc.io/repository/maven-public/")
        }

        maven {
            url = uri("https://nexuslite.gcnt.net/repos/paper/")
        }
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    dependencies {
        implementation("org.jetbrains:annotations:23.0.0")

        testImplementation(platform("org.junit:junit-bom:5.9.1"))
        testImplementation("org.junit.jupiter:junit-jupiter")
    }

    tasks.test {
        useJUnitPlatform()
    }

    tasks.compileJava {
        dependsOn(tasks.clean)
    }
}

subprojects.forEach { subproject ->
    evaluationDependsOn(subproject.path)
}

tasks.register<Jar>("allJar") {
    dependsOn(tasks.clean)
    dependsOn(subprojects.map { it.tasks.clean })
    dependsOn(tasks.compileJava)
    dependsOn(tasks.jar)
    dependsOn(subprojects.map { it.tasks.build })

    subprojects.forEach { subproject ->
        from(subproject.tasks.jar.get().outputs.files.map {
            zipTree(it)
        })
    }
}

val allJar: Task = tasks.named("allJar").get();

artifacts {
    add("archives", allJar)
}

publishing {
    publications {
        create<MavenPublication>("mavenJavaLocal") {
            artifact(allJar.outputs.files.singleFile)
        }
    }
}

tasks.named("generateMetadataFileForMavenJavaLocalPublication") {
    dependsOn(allJar)
}

val enablePublishing: Boolean = project.findProperty("enableUploadPublish")?.toString()?.toBoolean() == true

if (enablePublishing) {
    publishing {
        repositories {
            maven {
                name = "reposilite"
                url = uri("https://repo.tcoded.com/releases")

                credentials {
                    username = project.findProperty("REPOSILITE_USER")?.toString()
                        ?: System.getenv("REPOSILITE_USER")
                        ?: error("REPOSILITE_USER property or environment variable is not set")
                    password = project.findProperty("REPOSILITE_PASS")?.toString()
                        ?: System.getenv("REPOSILITE_PASS")
                        ?: error("REPOSILITE_PASS property or environment variable is not set")
                }

                authentication {
                    register<BasicAuthentication>("basic")
                }
            }
        }
    }

    tasks.named("publishMavenJavaLocalPublicationToReposiliteRepository") {
        dependsOn(tasks.jar)
        dependsOn(allJar)
    }
}