package de.flapdoodle.statik.pipeline.templates.pebble

import com.mitchellbosecke.pebble.extension.Filter
import com.mitchellbosecke.pebble.template.EvaluationContext
import com.mitchellbosecke.pebble.template.PebbleTemplate


class MapAsListFilter : Filter {
    override fun getArgumentNames(): MutableList<String> {
        return mutableListOf()
    }

    override fun apply(
        input: Any?,
        args: MutableMap<String, Any>,
        self: PebbleTemplate,
        context: EvaluationContext,
        lineNumber: Int
    ): Any? {
        if (input is Map<*,*>) {
            return input.map { it.key to it.value  }
        } else {
            if (input!=null) throw IllegalArgumentException("is not a map: $input")
        }
        return null
    }
}