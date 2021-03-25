package de.flapdoodle.statik.config

import de.flapdoodle.statik.filetypes.Attributes

data class PageDefinition(
    val id: String,
    val path: String,
    val pageSize: Long = 1L,
    val documents: List<String> = emptyList()
) {
    companion object {
        fun parse(id: String, root: Attributes.Node): PageDefinition {
            val path = root.values("path", String::class).single()
            val documents = root.findValues("documents", String::class) ?: emptyList()
            val pageSize: Number? = root.findValues("pageSize", Number::class)?.singleOrNull()

            return PageDefinition(
                id = id,
                path = path,
                pageSize = pageSize?.toLong() ?: 1L,
                documents = documents
            )
        }

    }
}