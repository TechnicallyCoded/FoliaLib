group = "com.tcoded.folialib.platform"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly("dev.folia:folia-api:1.19.4-R0.1-SNAPSHOT")

    implementation(project(":common"))
    implementation(project(":platform:common"))
}