plugins {
    kotlin("jvm")
}

group = "off.kys.instella.shared_env"
version = "1.0.0"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
}