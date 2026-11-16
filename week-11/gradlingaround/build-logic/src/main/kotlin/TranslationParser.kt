import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

class TranslationParser {

    fun parse(file: File): Map<String, String> {
        val result = mutableMapOf<String, String>()

        val document = createDocumentBuilder().parse(file)
        val root = document.documentElement

        val children = root.childNodes

        for (index in 0 until children.length) {
            val node = children.item(index)

            if (node !is Element) continue

            when (node.tagName) {
                "string" -> {
                    val name = node.getAttribute("name")
                    val value = node.textContent

                    result[name] = value
                }

                "plurals" -> {
                    val name = node.getAttribute("name")
                    val itemNodes = node.getElementsByTagName("item")
                    for (i in 0 until itemNodes.length) {
                        val item = itemNodes.item(i) as Element
                        result["$name.${item.getAttribute("quantity")}"] = item.textContent
                    }
                }

                "string-array" -> {
                    val name = node.getAttribute("name")
                    val itemNodes = node.getElementsByTagName("item")
                    for (i in 0 until itemNodes.length) {
                        result["$name.$i"] = itemNodes.item(i).textContent
                    }
                }
            }
        }

        return result
    }

    private fun createDocumentBuilder() =
        DocumentBuilderFactory.newInstance().apply {
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
        }.newDocumentBuilder()
}
