package de.flapdoodle.statik.config

import de.flapdoodle.statik.filetypes.Attributes
import de.flapdoodle.statik.types.asSet

data class PageDefinition(
    val id: String,
    val path: String,
    val pageSize: Int?,
    val template: String = id,
    val documents: Set<String> = emptySet(),
    val orderBy: Set<String> = emptySet()
) {
    init {
        require(pageSize == null || pageSize > 0) { "pageSize <= 0" }
        require(pageSize == null || orderBy.isNotEmpty()) { "orderBy must be set if pageSize!=1" }
    }

    companion object {
        fun parse(id: String, root: Attributes.Node): PageDefinition {
            val path = root.values("path", String::class).single()
            val documents = root.findValues("documents", String::class) ?: emptyList()
            val orderBy = root.findValues("orderBy", String::class) ?: emptyList()
            val pageSize: Number? = root.findValues("pageSize", Number::class)?.singleOrNull()
            val template = root.findValues("template", String::class)?.singleOrNull() ?: id

            return PageDefinition(
                id = id,
                path = path,
                pageSize = pageSize?.toInt(),
                template = template,
                documents = documents.asSet(),
                orderBy = orderBy.asSet()
            )
        }

    }
}