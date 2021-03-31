package de.flapdoodle.statik.config

import de.flapdoodle.statik.filetypes.Attributes

data class Site(val attributes: Attributes.Node) {
    companion object {
        fun parse(root: Attributes.Node): Site {
            return Site(root)
        }
    }
}