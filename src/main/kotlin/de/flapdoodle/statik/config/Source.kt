package de.flapdoodle.statik.config

import de.flapdoodle.statik.filetypes.Attributes

data class Source(
    val id: String,
    val paths: Set<String>,
    val type: SourceType = SourceType.Content
) {

    companion object {
        fun parse(id: String, root: Attributes.Node): Source {
            val paths = root.values("paths", String::class).toSet()
            val typeAttr = root.findValues("type", String::class)?.singleOrNull()
            val type = if (typeAttr != null)
                SourceType.byId(typeAttr)
            else
                SourceType.Content

            return Source(
                id = id,
                paths = paths,
                type = type
            )
        }
    }
}