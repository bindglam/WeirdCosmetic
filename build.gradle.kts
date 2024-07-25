plugins {
    kotlin("jvm") version "1.9.22"
    id("java")
}

group = "io.github.bindglam"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.oraxen.com/releases")
    maven("https://jitpack.io")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.dmulloy2.net/repository/public/")
    mavenLocal()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    compileOnly("io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc:spigot:1.21-R0.1-SNAPSHOT")

    compileOnly("io.th0rgal:oraxen:1.178.0")
    compileOnly("com.github.LoneDev6:API-ItemsAdder:3.6.3-beta-14")
    compileOnly("me.clip:placeholderapi:2.11.6")

    implementation(files("libs/ProtocolLib.jar"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}