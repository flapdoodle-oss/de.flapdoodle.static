package de.flapdoodle.statik.config

import de.flapdoodle.statik.filetypes.Attributes

data class Source(
    val id: String,
    val paths: Set<String>,
    val type: Type = Type.Content
) {

    enum class Type(val id: String) {
        Content("content"),
        Images("images");

        companion object {
            fun byId(id: String): Type {
                return values().find { it.id == id }
                    ?: throw IllegalAccessException("unknown type: $id")
            }
        }
    }

    companion object {
        fun parse(id: String, root: Attributes.Node): Source {
            val paths = root.values("paths", String::class).toSet()
            val typeAttr = root.findValues("type", String::class)?.singleOrNull()
            val type = if (typeAttr != null)
                Type.byId(typeAttr)
            else
                Type.Content

            return Source(
                id = id,
                paths = paths,
                type = type
            )
        }
    }
}