package de.flapdoodle.statik.config

import de.flapdoodle.statik.filetypes.Tree

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