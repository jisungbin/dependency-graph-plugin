/*
 * Developed by Ji Sungbin, 2023
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/dependency-graph-plugin/blob/main/LICENSE
 */

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

private const val SKIP = "#skip"

@CacheableTask
internal abstract class DependencyGraphPluginTask : DefaultTask() {
    abstract var config: DependencyGraphPluginConfig

    @get:InputDirectory
    abstract val source: DirectoryProperty

    @get:OutputDirectory
    abstract val destination: DirectoryProperty

    @TaskAction
    fun run() {
        val dot = destination.asFile.get().also { file ->
            if (file.parentFile?.exists() == false) file.parentFile!!.mkdirs()
            if (file.exists()) file.delete()
        }

        dot.appendText(
            text = """
             |digraph {
             |  graph [label="${config.projectName ?: project.rootProject.name}\n ",labelloc=t,fontsize=30,ranksep=1.4];
             |  node [style=filled, fillcolor="#bbbbbb"];
             |  rankdir=TB;
             |
            """.trimMargin(),
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

            (config.dependencyInfo.invoke(project) ?: DependencyInfo(
                color = config.defaultDependencyColor ?: SKIP,
            )).let { dependencyInfo ->
                if (dependencyInfo.color != SKIP) {
                    projectMapForDependencyInfo[project] = dependencyInfo
                }
            }

            project.configurations.all {
                if (name.lowercase().contains("test")) return@all

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

                        if (name.lowercase().endsWith("implementation")) {
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

            projectMapForDependencyInfo[project]?.let { (color, isBoxShape) ->
                if (isBoxShape) traits.add("shape=box")
                traits.add("fillcolor=\"$color\"")
            } ?: config.defaultDependencyColor?.let { color ->
                traits.add("fillcolor=\"$color\"")
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
                "-T${config.outputFormat.raw}",
                "-O",
                dot.path,
            )
        }

        if (config.autoDeleteDotFile) dot.delete()
        println("Project module dependency graph created at ${dot.absolutePath}.png")
    }
}
