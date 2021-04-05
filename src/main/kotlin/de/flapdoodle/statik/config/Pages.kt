package de.flapdoodle.statik.config

import de.flapdoodle.statik.filetypes.Attributes

data class Pages(
    val pageDefinitions: List<PageDefinition>
) {
    operator fun get(id: String): PageDefinition {
        return pageDefinitions.filter { it.id==id }.single()
    }

    companion object {
        fun parse(root: Attributes.Node): Pages {
            return Pages(
                pageDefinitions = root.nodeKeys().map { key ->
                    PageDefinition.parse(key, root.get(key, Attributes.Node::class))
                }
            )
        }
    }
}