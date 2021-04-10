package de.flapdoodle.statik.pipeline.templates.pebble

import com.mitchellbosecke.pebble.extension.Filter
import com.mitchellbosecke.pebble.template.EvaluationContext
import com.mitchellbosecke.pebble.template.PebbleTemplate


class SingleElementFilter(val allowNull: Boolean = false) : Filter {
    override fun getArgumentNames(): MutableList<String>? {
        return null
    }

    override fun apply(
        input: Any?,
        args: MutableMap<String, Any>?,
        self: PebbleTemplate?,
        context: EvaluationContext?,
        lineNumber: Int
    ): Any? {
        if (input == null) {
            return null
        }
        return if (input is Iterable<Any?>) {
            if (allowNull) input.singleOrNull() else input.single()
        } else {
            return null
        }
    }
}