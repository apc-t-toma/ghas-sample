plugins {
    kotlin("jvm") version "2.2.0"
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

    testImplementation(kotlin("test"))
    testImplementation(libs.junit.jupiter)
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}
