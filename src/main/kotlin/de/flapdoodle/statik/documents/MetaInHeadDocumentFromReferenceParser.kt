package de.flapdoodle.statik.documents

import de.flapdoodle.statik.files.ContentType
import de.flapdoodle.statik.files.Reference
import de.flapdoodle.statik.filetypes.Attributes
import de.flapdoodle.statik.filetypes.Toml
import de.flapdoodle.statik.filetypes.Yaml
import java.nio.file.Files
import java.util.regex.Pattern

object MetaInHeadDocumentFromReferenceParser : DocumentFromReference {
    private val TOML_START = Pattern.compile("(?m)(?d)^\\+{3}")
    private val TOML_END = TOML_START
    private val YAML_START = Pattern.compile("(?m)(?d)^-{3}")
    private val YAML_END = YAML_START

    override fun readFrom(reference: Reference): Document? {
        val fullContent = contentOf(reference)
        val contentType = contentTypeOf(reference)
        if (contentType != null) {
            val attributesAndContent = parseMeta(fullContent)
            if (attributesAndContent != null) {
                return Document(
                    reference = reference,
                    attributes = attributesAndContent.first,
                    contentType = contentType,
                    content = attributesAndContent.second
                )
            }
        }
        return null
    }

    private fun parseMeta(content: String): Pair<Attributes.Node, String>? {
        return parseTomlMeta(content) ?: parseYamlMeta(content)
    }

    private fun parseTomlMeta(content: String): Pair<Attributes.Node, String>? {
        val metaAndContent = partition(content, TOML_START, TOML_END)
        return metaAndContent?.let {
            Toml.asTree(it.first) to it.second
        }
    }

    private fun parseYamlMeta(content: String): Pair<Attributes.Node, String>? {
        val metaAndContent = partition(content, YAML_START, YAML_END)
        return metaAndContent?.let {
            Yaml.asTree(it.first) to it.second
        }
    }

    private fun partition(content: String, metaStart: Pattern, metaEnd: Pattern): Pair<String, String>? {
        val start = metaStart.matcher(content)
        val end = metaEnd.matcher(content)

        if (start.find()) {
            val startOfMeta = start.end()
            if (end.find(startOfMeta)) {
                val endOfMeta = end.start()
                val startOfContent = end.end()

                return content.substring(startOfMeta, endOfMeta) to content.substring(startOfContent)
            }
        }

        return null
    }

    private fun contentTypeOf(reference: Reference): ContentType? {
        val fileName = reference.path.fileName.toString()
        val idx = fileName.lastIndexOf('.')
        require(idx != -1) { "file extension missing: ${reference.path}" }
        val extension = fileName.substring(idx + 1)
        return ContentType.ofExtension(extension)
    }

    private fun contentOf(reference: Reference): String {
        return when (reference) {
            is Reference.File -> Files.readString(reference.path, Charsets.UTF_8)
            is Reference.SymLink -> Files.readString(reference.destination, Charsets.UTF_8)
        }
    }
}