package de.flapdoodle.statik.pipeline.templates.pebble

import com.mitchellbosecke.pebble.extension.Filter
import com.mitchellbosecke.pebble.template.EvaluationContext
import com.mitchellbosecke.pebble.template.PebbleTemplate


class KeyContainsFilter : Filter {
    override fun getArgumentNames(): MutableList<String> {
        return mutableListOf("key","value")
    }

    override fun apply(
        input: Any?,
        args: MutableMap<String, Any>,
        self: PebbleTemplate,
        context: EvaluationContext,
        lineNumber: Int
    ): Any? {
        val filterByKey = args["key"]
        val containsValue = args["value"]
        require(filterByKey!=null) {"key not set"}
        require(containsValue!=null) {"value not set"}

        if (input is List<*>) {
            val filtered = input.filter {
                if (it is Map<*, *>) {
                    val map: Map<*, *> = it
                    val value = map[filterByKey]
                    containsValue==value || (value is List<*> && value.contains(containsValue))
                } else {
                    false
                }
            }
            return filtered
        }
        return null
    }
}