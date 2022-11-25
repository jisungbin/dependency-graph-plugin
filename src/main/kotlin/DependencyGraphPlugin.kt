/*
 * Developed by Ji Sungbin, 2022
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/duckie-team/dependency-graph-plugin/blob/main/LICENSE
 */

import land.sungbin.dependency.graph.DependencyGraphPluginConfigs
import land.sungbin.dependency.graph.DependencyGraphTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register

class DependencyGraphPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val configs = project.extensions.create<DependencyGraphPluginConfigs>(
                name = "dependencyGraphConfigs",
            )
            afterEvaluate {
                tasks.register<DependencyGraphTask>(
                    name = "dependencyGraph",
                    configs,
                )
            }
        }
    }
}
