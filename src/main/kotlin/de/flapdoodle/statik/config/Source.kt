package de.flapdoodle.statik.config

import de.flapdoodle.statik.files.FileType
import de.flapdoodle.statik.filetypes.Attributes

data class Source(
    val id: String,
    val paths: Set<String>,
    val type: FileType = FileType.Text
) {

    companion object {
        fun parse(id: String, root: Attributes.Node): Source {
            val paths = root.values("paths", String::class).toSet()
            val typeAttr = root.findValues("type", String::class)?.singleOrNull()
            val type = if (typeAttr != null)
                FileType.byId(typeAttr)
            else
                FileType.Text

            return Source(
                id = id,
                paths = paths,
                type = type
            )
        }
    }
}