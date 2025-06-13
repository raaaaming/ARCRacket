plugins {
    alias(libs.plugins.kotlin.jvm)
    kotlin("plugin.serialization") version "2.1.20"
}

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
    gradlePluginPortal()
}

dependencies {
    api(libs.netty)
    implementation(libs.kotlin.serialization)
    implementation(libs.reflections)
    implementation(libs.kotlin.reflection)
    implementation(libs.logback)
}

java {
    withSourcesJar()
    withJavadocJar()
}