/*
 * Developed by Ji Sungbin, 2023
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/dependency-graph-plugin/blob/main/LICENSE
 */

@file:Suppress("unused")

import OutputFormat.PNG
import OutputFormat.SVG
import org.gradle.api.Project

/**
 * Graph image file generation formats.
 */
enum class OutputFormat(internal val raw: String) {
    PNG("png"),
    SVG("svg"),
    JPG("jpg"),
}

/**
 * A dependency information object.
 *
 * @param color **Hex** color of the dependency
 * @param isBoxShape Whether that dependency will be displayed as a rectangle on the graph.
 * The default value is `false`, and in case of `false`, it is displayed as an oval.
 */
data class DependencyInfo(
    val color: String,
    val isBoxShape: Boolean = false,
) {
    init {
        require(color.startsWith("#")) {
            "Hex colors must start with `#`"
        }
    }
}

/**
 * Calculate the policy to generate the graph.
 */
@DependencyGraphPluginDsl
open class DependencyGraphPluginConfig {
    internal var dependencyInfo: (project: Project) -> DependencyInfo? = { null }

    /**
     * The path where the dot file of the graph will be generated.
     * The png of the graph is generated along this path, too.
     *
     * Default is "generated/dependency-graph/project.dot".
     */
    open var dotFilePath = "generated/dependency-graph/project.dot"

    /**
     * Whether to automatically delete the dot file after
     * the png is created from the dot file of the graph.
     *
     * Default is `true`.
     */
    open var autoDeleteDotFile = true

    /**
     * Name to be displayed at the top of the graph.
     *
     * Default is `null`.
     * `null` means: `${project.rootProject.name}`
     */
    open var projectName: String? = null

    /**
     * The default **Hex** color to use when graphing uncolored dependencies.
     *
     * Defaults to `#eeeeee`, skipped graphing if `null` is provided.
     */
    open var defaultDependencyColor: String? = "#eeeeee"

    /**
     * The format in which the graph image will be generated.
     *
     * Default is [OutputFormat.PNG].
     */
    open var outputFormat: OutputFormat = PNG

    /**
     * Add dependency information to the graph.
     *
     * @param builder The lambda that calculates the dependency information.
     *  - project: Instances of the [project][Project] to compute information
     *
     * @return A dependency information object. See [DependencyInfo].
     */
    fun dependencyBuilder(builder: (project: Project) -> DependencyInfo?) {
        dependencyInfo = builder
    }
}

