/*
 * Developed by Ji Sungbin, 2022
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/duckie-team/dependency-graph-plugin/blob/main/LICENSE
 */

plugins {
    java
    id("project.two")
}

dependencies {
    implementation(project(":project-one"))
}