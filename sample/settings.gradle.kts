/*
 * Developed by Ji Sungbin, 2022
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/duckie-team/dependency-graph-plugin/blob/main/LICENSE
 */

@file:Suppress("UnstableApiUsage")

rootProject.name = "sample"

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }

    pluginManagement {
        repositories {
            mavenLocal()
            mavenCentral()
            gradlePluginPortal()
        }
    }

    includeBuild("local-plugins")
}

include(
    ":project-one",
    ":project-two",
    ":project-three",
)
