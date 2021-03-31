package de.flapdoodle.statik.pipeline.templates.pebble

import com.mitchellbosecke.pebble.attributes.AttributeResolver
import com.mitchellbosecke.pebble.attributes.ResolvedAttribute
import com.mitchellbosecke.pebble.node.ArgumentsNode
import com.mitchellbosecke.pebble.template.EvaluationContextImpl
import de.flapdoodle.statik.pipeline.templates.wrapper.HasAttributes

class HasAttributesAttributeResolver : AttributeResolver {
    override fun resolve(
        instance: Any?,
        attributeNameValue: Any?,
        argumentValues: Array<out Any>?,
        args: ArgumentsNode?,
        context: EvaluationContextImpl,
        filename: String,
        lineNumber: Int
    ): ResolvedAttribute? {
        if (instance is HasAttributes) {
            val attr = instance.findAttribute(attributeNameValue, argumentValues)
            if (attr != null) {
                return ResolvedAttribute(attr.wrapped)
            }
        }
        return null
    }
}