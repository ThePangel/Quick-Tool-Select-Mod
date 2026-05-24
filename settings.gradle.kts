pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.kikugie.dev/releases") { name = "KikuGie Releases" }
        maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie Snapshots" }
    }
}


plugins {
    id("dev.kikugie.stonecutter") version "0.9.4"
    id("dev.kikugie.loom-back-compat") version "0.3"
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"

}

gradle.beforeProject {
    buildscript.repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.kikugie.dev/releases")
        maven("https://maven.kikugie.dev/snapshots")
    }
}

stonecutter {
    create(rootProject) {
        versions(
            "1.21.1",
            "1.21.11",
            "26.1",


            )
        version(
            "26.1-barebones",
            "26.1"
        )
        version(
            "1.21.1-barebones",
            "1.21.1"
        )
        version(
            "1.21.11-barebones",
            "1.21.11"
        )
        vcsVersion = "26.1"

    }

}

rootProject.name = "QuickToolSelect"
