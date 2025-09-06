group = "com.tcoded.folialib.platform"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    compileOnly("dev.folia:folia-api:1.21.8-R0.1-SNAPSHOT")

    implementation(project(":common"))
    implementation(project(":platform:common"))
}
