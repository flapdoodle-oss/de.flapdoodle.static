package de.flapdoodle.statik.pipeline.generate

import de.flapdoodle.statik.path.Path

class DefaultRenderPath : RenderPath {
    override fun render(
        baseUrl: String,
        path: Path,
        propertyLookup: (String) -> Any?,
        formatterLookup: (property: String, formatterName: String?) -> Formatter
    ): String {
        val properties = path.propertyNames().toSet()
            .map { it to propertyLookup(it) }.toMap()
        
        val missingProperties = path.propertyNames().toSet().subtract(properties.keys)

        require(missingProperties.isEmpty()) {"missing properties: $missingProperties"}

        var skipEverythingBecauseFirstPageWillNotBeRenderer = false

        val sb=StringBuilder()
        sb.append(baseUrl)

        // TODO refactor to flapmap -> join
        path.parts.forEach { part ->
            when (part) {
                is Path.Part.Static -> {
                    if (!skipEverythingBecauseFirstPageWillNotBeRenderer) {
                        sb.append(urlify(part.fixed))
                    }
                }
                is Path.Part.Property -> {
                    val propertyValue = properties[part.property] ?: throw IllegalArgumentException("property not found: ${part.property}")
                    val formatter = formatterLookup(part.property, part.formatter)
                    val formattedValue = formatter.format(propertyValue)
                        ?: throw IllegalArgumentException("could not format $propertyValue (${propertyValue.javaClass}) with $formatter")
                    if (part.property==Path.PAGE && formattedValue=="1") {
                        skipEverythingBecauseFirstPageWillNotBeRenderer=true
                    }
                    if (!skipEverythingBecauseFirstPageWillNotBeRenderer) {
                        sb.append(urlify(formattedValue))
                    }
                }
            }
        }

        return sb.toString()
    }

    private fun urlify(src: String): String {
        return src.replace(' ', '-')
            .replace("--", "-")
            .toLowerCase()
            .replace("[^a-zA-Z0-9/\\-_.]".toRegex(), "")
    }
}