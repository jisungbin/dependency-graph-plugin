import io.kotest.core.spec.style.StringSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.string.shouldContain
import java.io.File
import org.gradle.testkit.runner.GradleRunner

class ConfigurationCacheTest : StringSpec() {
    private val testProjectDir = tempdir().resolve("project").also(File::mkdir)

    init {
        "두 번째 빌드는 configuration cache가 적용돼야 함" {
            val settingFile = File(testProjectDir, "settings.gradle.kts").apply {
                writeText(
                    """
rootProject.name = "test-project"
                    """.trimIndent(),
                )
            }
            val buildFile = File(testProjectDir, "build.gradle.kts").apply {
                writeText(
                    """
plugins {
    id("land.sungbin.dependency.graph.plugin")
}
                    """.trimIndent(),
                )
            }
            settingFile.createNewFile()
            buildFile.createNewFile()

            GradleRunner
                .create()
                .withProjectDir(testProjectDir)
                .withArguments("--configuration-cache", "dependencyGraph")
                .build()

            val result = GradleRunner
                .create()
                .withProjectDir(testProjectDir)
                .withArguments("--configuration-cache", "dependencyGraph")
                .build()

            result.output shouldContain "Reusing configuration cache."
        }
    }
}
