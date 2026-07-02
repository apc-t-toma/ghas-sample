plugins {
    kotlin("jvm") version "2.3.21"
}

group = "com.example.ghas"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    // Intentionally old libraries for Dependabot and alert verification.
    implementation(libs.commons.io)
    implementation(libs.jackson.databind)
    implementation(libs.spring.web)

    // Additional libraries for comprehensive Dependabot PR Triage test scenarios
    implementation(libs.commons.lang)    // Breaking change scenario (3.x -> 4.x)
    implementation(libs.log4j)            // Known vulnerability scenario
    implementation(libs.gson)             // Deprecated API usage scenario
    implementation(libs.okhttp)           // Security patch / minor version scenario
    implementation(libs.httpclient)       // Minor version compatibility scenario

    testImplementation(kotlin("test"))
    testImplementation(libs.junit.jupiter)
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}
