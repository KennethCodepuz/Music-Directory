plugins {
    kotlin("jvm") version "2.2.10"
    kotlin("plugin.serialization") version "2.2.10"
    application
}

val ktorVersion = "3.2.3"

dependencies {
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("org.postgresql:postgresql:42.7.4")
    implementation("ch.qos.logback:logback-classic:1.5.12")
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("musicdirectory.ApplicationKt")
}
