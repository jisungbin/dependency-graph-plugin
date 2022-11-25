/*
 * Developed by Ji Sungbin, 2022
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/duckie-team/dependency-graph-plugin/blob/main/LICENSE
 */

package land.sungbin.dependency.graph

import java.io.File
import java.util.Locale
import javax.inject.Inject
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.tasks.TaskAction

internal abstract class DependencyGraphPluginTask @Inject constructor(
    private val configs: DependencyGraphPluginConfigs,
) : DefaultTask() {
    @TaskAction
    fun run() {
        val dot = File(project.rootProject.rootDir, configs.dotFilePath)
        dot.parentFile.deleteRecursively()
        dot.parentFile.mkdirs()

        dot.appendText(
            """
             |digraph {
             |  graph [label="${configs.projectName ?: project.rootProject.name}\n ",labelloc=t,fontsize=30,ranksep=1.4];
             |  node [style=filled, fillcolor="#bbbbbb"];
             |  rankdir=TB;
             |
            """.trimMargin()
        )

        val rootProjects = mutableListOf<Project>()
        val queue = mutableListOf(project.rootProject)
        while (queue.isNotEmpty()) {
            val project = queue.removeAt(0)
            rootProjects.add(project)
            queue.addAll(project.childProjects.values)
        }
        queue.add(project.rootProject)

        val dependencyProjects = LinkedHashSet<Project>()
        val dependencies = LinkedHashMap<Pair<Project, Project>, MutableList<String>>()
        val projectMapForDependencyInfo = mutableMapOf<Project, DependencyInfo>()

        while (queue.isNotEmpty()) {
            val project = queue.removeAt(0)
            queue.addAll(project.childProjects.values)

            configs.dependencyInfo.invoke(project)?.let { dependencyInfo ->
                projectMapForDependencyInfo[project] = dependencyInfo
            }

            project.configurations.all {
                if (name.toLowerCase(Locale.getDefault()).contains("test")) {
                    return@all
                }
                getDependencies()
                    .withType(ProjectDependency::class.java)
                    .map(ProjectDependency::getDependencyProject)
                    .forEach { dependency ->
                        dependencyProjects.add(project)
                        dependencyProjects.add(dependency)
                        rootProjects.remove(dependency)

                        val graphKey = project to dependency
                        val traits = dependencies.computeIfAbsent(graphKey) {
                            mutableListOf()
                        }

                        if (name.toLowerCase(Locale.getDefault()).endsWith("implementation")) {
                            traits.add("style=dotted")
                        }
                    }
            }
        }

        dependencyProjects.sortedBy(Project::getPath).also { sortedDependencyProjects ->
            dependencyProjects.clear()
            dependencyProjects.addAll(sortedDependencyProjects)
        }

        dot.appendText("\n  # Projects\n\n")

        for (project in dependencyProjects) {
            val traits = mutableListOf<String>()

            projectMapForDependencyInfo[project]?.let { dependencyInfo ->
                val (color, isBoxShape) = dependencyInfo
                if (isBoxShape) {
                    traits.add("shape=box")
                }
                traits.add("fillcolor=\"$color\"")
            } ?: run {
                configs.defaultDependencyColor?.let { color ->
                    traits.add("fillcolor=\"$color\"")
                }
            }

            dot.appendText("  \"${project.path}\" [${traits.joinToString(", ")}];\n")
        }

        dot.appendText("\n  {rank = same;")

        for (project in dependencyProjects) {
            if (rootProjects.contains(project)) {
                dot.appendText(" \"${project.path}\";")
            }
        }

        dot.appendText("}\n")
        dot.appendText("\n  # Dependencies\n\n")

        dependencies.forEach { (key, traits) ->
            dot.appendText("  \"${key.first.path}\" -> \"${key.second.path}\"")
            if (traits.isNotEmpty()) {
                dot.appendText(" [${traits.joinToString(", ")}]")
            }
            dot.appendText("\n")
        }

        dot.appendText("}\n")

        project.rootProject.exec {
            commandLine = listOf(
                "dot",
                "-Tpng",
                "-O",
                dot.path,
            )
        }

        if (configs.autoDeleteDotFile) {
            dot.delete()
        }
        println("Project module dependency graph created at ${dot.absolutePath}.png")
    }
}
