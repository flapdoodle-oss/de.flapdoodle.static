package de.flapdoodle.statik.config

import de.flapdoodle.statik.filetypes.Tree
import java.nio.file.Path

data class Sources(
    val basePath: Path,
    val sources: List<Source>
) {

    companion object {
        fun parse(basePath: Path, root: Tree.Node): Sources {
            return Sources(
                basePath = basePath,
                sources = root.keys().map {
                    Source.parse(it, root.get(it, Tree.Node::class))
                }
            )
        }
    }
}