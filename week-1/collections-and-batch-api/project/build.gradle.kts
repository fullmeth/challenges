plugins {
    kotlin("jvm") version "2.4.10"
    kotlin("plugin.allopen") version "2.2.0"
    id("org.jetbrains.kotlinx.benchmark") version "0.4.17"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-benchmark-runtime:0.4.17")
    testImplementation(kotlin("test"))
}

allOpen {
    annotation("org.openjdk.jmh.annotations.State")
}

benchmark {
    targets {
        register("main")
    }
}

kotlin {
    jvmToolchain(26)
}

tasks.test {
    useJUnitPlatform()
}