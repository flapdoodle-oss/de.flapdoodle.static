package de.flapdoodle.statik.config

import de.flapdoodle.statik.filetypes.Attributes
import java.nio.file.Path

data class Sources(
    val basePath: Path,
    val sources: List<Source>
) {

    companion object {
        fun parse(basePath: Path, root: Attributes.Node): Sources {
            return Sources(
                basePath = basePath,
                sources = root.keys().map {
                    Source.parse(it, root.get(it, Attributes.Node::class))
                }
            )
        }
    }
}