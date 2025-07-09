@file:Suppress("SpellCheckingInspection")

plugins {
    kotlin("jvm")
}

group = "off.kys.itoml4kt"
version = "1.0.0"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(11)
}