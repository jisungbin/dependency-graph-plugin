/*
 * Developed by Ji Sungbin, 2022
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/duckie-team/dependency-graph-plugin/blob/main/LICENSE
 */

import land.sungbin.dependency.graph.DependencyInfo
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
    `kotlin-dsl`
    id("land.sungbin.dependency.graph.plugin") version "1.0.0"
}

dependencyGraphConfigs {
    projectName = "dependency-graph-sample"

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

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

tasks.withType<JavaCompile> {
    sourceCompatibility = JavaVersion.VERSION_11.toString()
    targetCompatibility = JavaVersion.VERSION_11.toString()
}