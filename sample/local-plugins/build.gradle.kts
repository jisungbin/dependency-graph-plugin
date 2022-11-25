/*
 * Designed and developed by Duckie Team, 2022
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/duckie-team/duckie-android/blob/develop/LICENSE
 */

plugins {
    `kotlin-dsl`
}

group = "local.plugins"

gradlePlugin {
    plugins {
        register("projectOne") {
            id = "project.one"
            implementationClass = "ProjectOnePlugin"
        }
        register("projectTwo") {
            id = "project.two"
            implementationClass = "ProjectTwoPlugin"
        }
        register("projectThree") {
            id = "project.three"
            implementationClass = "ProjectThreePlugin"
        }
    }
}
