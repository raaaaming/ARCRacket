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
    api(project(":modules:client"))
    api(project(":modules:server"))
}

java {
    withSourcesJar() // 소스 jar 생성
    withJavadocJar() // API 문서 추가
}