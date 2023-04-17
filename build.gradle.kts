/*
 * Developed by Ji Sungbin, 2023
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/dependency-graph-plugin/blob/main/LICENSE
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.20"
    `kotlin-dsl`
    `maven-publish`
    id("com.vanniktech.maven.publish") version "0.25.1"
}

gradlePlugin {
    plugins {
        create("dependencyGraphPlugin") {
            id = "land.sungbin.dependency.graph.plugin"
            implementationClass = "DependencyGraphPlugin"
        }
    }
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

tasks.withType<JavaCompile> {
    sourceCompatibility = JavaVersion.VERSION_11.toString()
    targetCompatibility = JavaVersion.VERSION_11.toString()
}
