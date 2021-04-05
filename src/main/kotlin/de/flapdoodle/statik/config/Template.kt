package de.flapdoodle.statik.config

import de.flapdoodle.statik.filetypes.Attributes

data class Template(
    val path: String,
    val resources: List<TemplateResources>
) {
    companion object {
        fun parse(root: Attributes.Node): Template {
            val templatePath = root.values("path", String::class).single()
            return Template(
                path = templatePath,
                resources = root.nodeKeys().map { key ->
                    TemplateResources.parse(key, root.get(key, Attributes.Node::class))
                }
            )
        }
    }

}