import com.google.gson.GsonBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction

abstract class ExportTranslationsTask : DefaultTask() {

    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val resourceFiles: ConfigurableFileCollection

    @get:Input
    abstract val moduleDirectories: MapProperty<String, String>

    @get:Internal
    abstract val rootDirectory: DirectoryProperty

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @TaskAction
    fun execute() {
        val parser = TranslationParser()

        val translations = mutableMapOf<String, MutableMap<String, String>>()

        val root = rootDirectory.get().asFile.toPath()

        val modules = moduleDirectories.get().map { (module, directory) ->
            module to root.resolve(directory).normalize()
        }

        resourceFiles.forEach { file ->
            val filePath = file.toPath().normalize()

            val module = modules.find { (_, path) ->
                filePath.startsWith(path)
            }?.first ?: return@forEach

            val locale = resolveLocale(file.parentFile.name)
                ?: return@forEach

            val moduleName = module
                .removePrefix(":")
                .replace(':', '.')

            val localeTranslations = translations.getOrPut(locale) {
                mutableMapOf()
            }

            parser.parse(file).forEach { (key, value) ->
                val fullKey = "$moduleName.$key"

                localeTranslations[fullKey] = value
            }
        }


        val gson = GsonBuilder()
            .setPrettyPrinting()
            .create()

        val output = outputDirectory.get().asFile

        output.mkdirs()

        output.listFiles()
            ?.filter { it.isFile && it.extension == "json" }
            ?.forEach { it.delete() }

        translations.forEach { (locale, entries) ->
            val file = output.resolve("$locale.json")

            file.writeText(
                gson.toJson(entries.toSortedMap()),
                Charsets.UTF_8
            )
        }


    }

    private fun resolveLocale(directoryName: String): String? {
        return when {
            directoryName == "values" -> "en"

            directoryName.startsWith("values-") -> {
                val locale = directoryName.removePrefix("values-")
                locale.takeIf {
                    it in setOf("en", "uk", "fr")
                }
            }

            else -> null
        }
    }
}