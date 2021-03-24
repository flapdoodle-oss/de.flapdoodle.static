package de.flapdoodle.statik.config

import de.flapdoodle.statik.filetypes.Tree

data class Sources(
    val sources: List<Source>
) {

    companion object {
        fun parse(root: Tree.Node): Sources {
            return Sources(
                sources = root.keys().map {
                    Source.parse(it, root.get(it, Tree.Node::class))
                }
            )
        }
    }
}