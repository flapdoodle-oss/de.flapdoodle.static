package de.flapdoodle.statik.pipeline.templates.pebble

import com.mitchellbosecke.pebble.attributes.AttributeResolver
import com.mitchellbosecke.pebble.extension.AbstractExtension
import com.mitchellbosecke.pebble.extension.Filter

class CustomExtension : AbstractExtension() {
    override fun getAttributeResolver(): MutableList<AttributeResolver> {
        return mutableListOf(HasAttributesAttributeResolver())
    }

    override fun getFilters(): MutableMap<String, Filter> {
        return mutableMapOf(
            "single" to SingleElementFilter(),
            "singleOrNull" to SingleElementFilter(true),
            "allWithKey" to HasKeyFilter(),
            "keyContains" to KeyContainsFilter(),
            "mapAsList" to MapAsListFilter()
        )
    }

//    class AttributesTreeResolver : AttributeResolver {
//        override fun resolve(
//            instance: Any?,
//            attributeNameValue: Any?,
//            argumentValues: Array<out Any>?,
//            args: ArgumentsNode?,
//            context: EvaluationContextImpl,
//            filename: String,
//            lineNumber: Int
//        ): ResolvedAttribute? {
//            if (instance is Attributes.Node) {
//                if (attributeNameValue is String) {
//                    require(argumentValues == null) { "argumentValues set to $argumentValues" }
//                    require(args == null) { "args set to $args" }
//
//                    val resolved = instance.find(attributeNameValue, Attributes::class)
//                    if (resolved != null) {
//                        return when (resolved) {
//                            is Attributes.Node -> ResolvedAttribute(resolved)
//                            is Attributes.Values<*> -> ResolvedAttribute(resolved.values)
//                        }
//                    }
//                }
//            }
//
//            return null
//        }
//    }
}