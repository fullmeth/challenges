import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register

class ExportTranslationsPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.tasks.register<ExportTranslationsTask>(
            "exportTranslations"
        ) {
            group = "custom"
            description = "export xml strings to json"

            target.subprojects.forEach { subproject ->
                val resourceDirectory =
                    subproject.layout.projectDirectory
                        .dir("src/main/res")

                val resources = subproject.fileTree(
                    resourceDirectory
                ) {
                    include("values*/*.xml")
                }

                resourceFiles.from(resources)

                moduleDirectories.put(
                    subproject.path,
                    resourceDirectory.asFile
                        .relativeTo(target.projectDir)
                        .invariantSeparatorsPath
                )
            }

            rootDirectory.set(target.layout.projectDirectory)

            outputDirectory.set(
                target.layout.buildDirectory.dir("translations")
            )
        }
    }
}