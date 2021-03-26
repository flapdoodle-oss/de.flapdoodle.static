package de.flapdoodle.statik.pipeline.generate

import de.flapdoodle.statik.path.Path

interface RenderPath {
    fun render(
        path: Path,
        properties: Map<String, Any>,
        formatterLookup: (property: String, formatterName: String?) -> Formatter
    ): String
}