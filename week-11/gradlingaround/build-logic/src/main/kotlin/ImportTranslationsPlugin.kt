import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register

class ImportTranslationsPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.tasks.register<ImportTranslationsTask>(
            "importTranslations"
        ) {
            group = "custom"
            description = "import from json to xml"

            inputDirectory.set(
                target.layout.buildDirectory.dir("translations")
            )

            rootDirectory.set(target.layout.projectDirectory)

            target.subprojects.forEach { subproject ->
                val resourceDirectory =
                    subproject.layout.projectDirectory.dir("src/main/res")

                moduleDirectories.put(
                    subproject.path,
                    resourceDirectory.asFile
                        .relativeTo(target.projectDir)
                        .invariantSeparatorsPath
                )
            }
        }
    }
}