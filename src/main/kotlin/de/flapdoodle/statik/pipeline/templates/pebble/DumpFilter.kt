package de.flapdoodle.statik.pipeline.templates.pebble

import com.mitchellbosecke.pebble.extension.Filter
import com.mitchellbosecke.pebble.template.EvaluationContext
import com.mitchellbosecke.pebble.template.PebbleTemplate


class DumpFilter : Filter {
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
        return if (input!=null)
            "${input.javaClass}: $input"
        else
            null
    }
}