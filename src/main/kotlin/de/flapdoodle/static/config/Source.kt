package de.flapdoodle.static.config

import de.flapdoodle.static.filetypes.Tree
import java.util.regex.Pattern

data class Source(
    val id: String,
    val paths: Set<String>,
    val keys: Set<String>
) {
    companion object {
        fun parse(id: String, root: Tree.Node): Source {
            val paths = root.get("paths", Tree.Array::class)
                .asListOf(String::class).toSet()
            val keys = root.get("keys",Tree.Array::class)
                .asListOf(String::class).toSet()
            return Source(id,paths,keys)
        }

    }
}