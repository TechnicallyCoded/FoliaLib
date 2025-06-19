group = "com.tcoded.folialib.platform"

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.13.2-R0.1-SNAPSHOT")

    implementation(project(":common"))
    implementation(project(":platform:common"))
}