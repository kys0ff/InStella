plugins {
    kotlin("jvm")
}

group = "off.kys.instella.plugin_bridge"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.luaj.jse)
    implementation(project(":shared-env"))
    implementation(project(":shared-logger"))
    implementation(project(":toml4kt"))
}

kotlin {
    jvmToolchain(17)
}