/*
 * Developed by Ji Sungbin, 2022
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/duckie-team/dependency-graph-plugin/blob/main/LICENSE
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
}

group = "land.sungbin.dependency.graph"
version = "1.0.0"

repositories {
    mavenCentral()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}