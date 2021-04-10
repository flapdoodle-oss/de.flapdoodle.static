package de.flapdoodle.statik.pipeline.templates.pebble

import com.mitchellbosecke.pebble.extension.Filter
import com.mitchellbosecke.pebble.template.EvaluationContext
import com.mitchellbosecke.pebble.template.PebbleTemplate


class HasKeyFilter : Filter {
    override fun getArgumentNames(): MutableList<String> {
        return mutableListOf("key")
    }

    override fun apply(
        input: Any?,
        args: MutableMap<String, Any>,
        self: PebbleTemplate,
        context: EvaluationContext,
        lineNumber: Int
    ): Any? {
        val filterByKey = args["key"]
        require(filterByKey!=null) {"key not set"}

        if (input is List<*>) {
            val filtered = input.filter {
                if (it is Map<*, *>) {
                    val map: Map<*, *> = it
                    map.containsKey(filterByKey)
                } else {
                    false
                }
            }
            return filtered
        }
        return null
    }
}