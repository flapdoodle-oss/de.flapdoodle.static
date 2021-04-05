package de.flapdoodle.statik.config

import de.flapdoodle.statik.filetypes.Attributes

data class TemplateResources(
    val id: String,
    val paths: Set<String>,
) {
    companion object {
        fun parse(id: String, root: Attributes.Node): TemplateResources {
            val paths = root.values("paths", String::class).toSet()

            return TemplateResources(
                id = id,
                paths = paths,
            )
        }
    }
}