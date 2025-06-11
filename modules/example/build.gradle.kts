plugins {
    alias(libs.plugins.kotlin.jvm)
}

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
    gradlePluginPortal()
}

dependencies {
    api(project(":modules:core"))
    api(project(":modules:server"))
    api(project(":modules:client"))
}