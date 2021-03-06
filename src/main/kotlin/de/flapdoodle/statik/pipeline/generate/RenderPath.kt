package de.flapdoodle.statik.pipeline.generate

import de.flapdoodle.statik.path.Path

interface RenderPath {
    fun render(
        baseUrl: String,
        path: Path,
        propertyLookup: (String) -> Any?,
        formatterLookup: (property: String, formatterName: String?) -> Formatter
    ): String
}