/*
 * Developed by Ji Sungbin, 2023
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/dependency-graph-plugin/blob/main/LICENSE
 */

@file:Suppress("unused")

import java.io.File
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register

class DependencyGraphPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            val config = extensions.create<DependencyGraphPluginConfig>("dependencyGraphConfig")
            afterEvaluate {
                tasks.register<DependencyGraphPluginTask>("dependencyGraph") {
                    this.config = config
                    source.set(buildDir)
                    destination.set(File(rootProject.rootDir, config.dotFilePath))
                }
            }
        }
    }
}
