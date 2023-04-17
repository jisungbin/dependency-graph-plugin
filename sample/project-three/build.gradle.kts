/*
 * Developed by Ji Sungbin, 2023
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/dependency-graph-plugin/blob/main/LICENSE
 */

plugins {
    java
    id("project.three")
}

dependencies {
    implementation(project(":project-one"))
    implementation(project(":project-two"))
}