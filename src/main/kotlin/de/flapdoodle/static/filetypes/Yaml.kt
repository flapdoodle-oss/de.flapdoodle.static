package de.flapdoodle.static.filetypes

import java.util.*
import org.yaml.snakeyaml.Yaml as WRAPPED

object Yaml {
    fun asTree(raw: String): Tree.Node {
        val wrapped = WRAPPED().load<Map<String,Any>>(raw) ?: emptyMap()
        return asTree(wrapped)
    }

    private fun asTree(yaml: Map<String, Any>): Tree.Node {
        val children = yaml.map {
            val value: Any = it.value
            it.key to when (value) {
                is Map<*,*> -> asTree(value as Map<String, Any>)
                is List<*> -> asArray(value)
                is String, is Int, is Date -> asArray(listOf(value))
                else -> throw IllegalArgumentException("not implemented: ${value.javaClass}")
            }
        }.toMap()

        return Tree.Node(children)
    }

    private fun <T> asArray(value: List<T>): Tree.Values<T> {
        return Tree.Values(value)
    }

}