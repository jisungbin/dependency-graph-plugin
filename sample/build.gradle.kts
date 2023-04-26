/*
 * Developed by Ji Sungbin, 2023
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/dependency-graph-plugin/blob/main/LICENSE
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.21"
    `kotlin-dsl`
    id("land.sungbin.dependency.graph.plugin") version "1.1.0"
}

dependencyGraphConfig {
    projectName = "dependency-graph-sample"
    outputFormat = OutputFormat.SVG

    dependencyBuilder { project ->
        with(project.plugins) {
            when {
                hasPlugin("project.three") -> DependencyInfo("#81d4fa", isBoxShape = true)
                hasPlugin("project.two") -> DependencyInfo("#ffc9ba")
                hasPlugin("project.one") -> DependencyInfo("#fcb96a")
                else -> null
            }
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