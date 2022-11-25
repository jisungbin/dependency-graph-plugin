/*
* Designed and developed by Duckie Team, 2022
*
* Licensed under the MIT.
* Please see full license: https://github.com/duckie-team/duckie-android/blob/develop/LICENSE
*/

@file:Suppress("UnstableApiUsage")

rootProject.name = "sample"

pluginManagement {
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
