/*
 * Developed by Ji Sungbin, 2023
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/dependency-graph-plugin/blob/main/LICENSE
 */

@file:Suppress("unused")

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register

class DependencyGraphPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            val configs = project.extensions.create<DependencyGraphPluginConfig>(
                name = "dependencyGraphConfig",
            )
            afterEvaluate {
                tasks.register<DependencyGraphPluginTask>(
                    name = "dependencyGraph",
                    configs,
                )
            }
        }
    }
}
