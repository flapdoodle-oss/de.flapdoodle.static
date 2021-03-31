package de.flapdoodle.statik.pipeline.templates.wrapper

import com.mitchellbosecke.pebble.attributes.ResolvedAttribute
import de.flapdoodle.statik.config.Site
import de.flapdoodle.statik.filetypes.Attributes

data class SiteWrapper(val site: Site) : HasAttributes {
    override fun findAttribute(attributeName: Any?, argumentValues: Array<out Any>?): IsAttribute? {
        if (attributeName is String) {
            require(argumentValues==null) {"argumentValues expected to be null: $argumentValues"}
            
            val resolved = site.attributes.find(attributeName, Attributes::class)
            if (resolved != null) {
                return when (resolved) {
                    is Attributes.Node -> IsAttribute(resolved)
                    is Attributes.Values<*> -> IsAttribute(resolved.values)
                }
            }
        }
        return null
    }
}