package de.flapdoodle.static.config

import de.flapdoodle.static.filetypes.Toml
import de.flapdoodle.static.filetypes.Tree

data class Config(
    val sources: Sources
) {

    companion object {
        fun parse(source: Tree.Node): Config {
            val sourceConfig = source.get("sources", Tree.Node::class)
            return Config(
                sources = Sources.parse(sourceConfig)
            )
        }
    }
}