
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.w3c.dom.Element
import java.io.File
import javax.xml.XMLConstants
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

abstract class ImportTranslationsTask : DefaultTask() {

    @get:InputDirectory
    abstract val inputDirectory: DirectoryProperty

    @get:Input
    abstract val moduleDirectories: MapProperty<String, String>

    @get:Internal
    abstract val rootDirectory: DirectoryProperty

    @TaskAction
    fun importTranslations() {
        val root = rootDirectory.get().asFile.toPath()
        val gson = Gson()
        val type = object : TypeToken<Map<String, String>>() {}.type

        inputDirectory.get().asFile
            .listFiles { file -> file.extension == "json" }
            .orEmpty()
            .forEach { jsonFile ->
                val locale = jsonFile.nameWithoutExtension
                val valuesDirectory = if (locale == "en") {
                    "values"
                } else {
                    "values-$locale"
                }

                val translations: Map<String, String> =
                    gson.fromJson(jsonFile.readText(), type)

                moduleDirectories.get().forEach { (module, directory) ->
                    val prefix = module.removePrefix(":")
                        .replace(':', '.') + "."

                    val entries = translations
                        .filterKeys { it.startsWith(prefix) }
                        .mapKeys { (key, _) -> key.removePrefix(prefix) }

                    if (entries.isEmpty()) return@forEach

                    val resDirectory = root.resolve(directory).toFile()
                    val xmlFiles = resDirectory.resolve(valuesDirectory)
                        .listFiles { file -> file.extension == "xml" }
                        .orEmpty()

                    xmlFiles.forEach { xmlFile ->
                        updateXml(xmlFile, entries)
                    }
                }
            }
    }

    private fun updateXml(
        file: File,
        translations: Map<String, String>
    ) {
        val factory = DocumentBuilderFactory.newInstance().apply {
            setFeature(
                "http://apache.org/xml/features/disallow-doctype-decl",
                true
            )
            setFeature(
                "http://xml.org/sax/features/external-general-entities",
                false
            )
            setFeature(
                "http://xml.org/sax/features/external-parameter-entities",
                false
            )
            isXIncludeAware = false
            isExpandEntityReferences = false
        }

        val document = factory.newDocumentBuilder().parse(file)
        val root = document.documentElement
        var changed = false

        for (i in 0 until root.childNodes.length) {
            val node = root.childNodes.item(i)
            if (node !is Element) continue

            val name = node.getAttribute("name")

            when (node.tagName) {
                "string" -> {
                    translations[name]?.let { value ->
                        node.textContent = value
                        changed = true
                    }
                }

                "plurals" -> {
                    val items = node.getElementsByTagName("item")

                    for (j in 0 until items.length) {
                        val item = items.item(j) as Element
                        val quantity = item.getAttribute("quantity")

                        translations["$name.$quantity"]?.let { value ->
                            item.textContent = value
                            changed = true
                        }
                    }
                }

                "string-array" -> {
                    val items = node.getElementsByTagName("item")

                    for (j in 0 until items.length) {
                        translations["$name.$j"]?.let { value ->
                            items.item(j).textContent = value
                            changed = true
                        }
                    }
                }
            }
        }

        if (!changed) return

        val transformerFactory = TransformerFactory.newInstance().apply {
            setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true)
            setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "")
            setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "")
        }

        transformerFactory.newTransformer().apply {
            setOutputProperty(OutputKeys.INDENT, "yes")
            setOutputProperty(OutputKeys.ENCODING, "UTF-8")
        }.transform(
            DOMSource(document),
            StreamResult(file)
        )
    }
}
