package de.flapdoodle.static.filetypes

import kotlin.reflect.KClass
import kotlin.reflect.cast
import com.moandjiezana.toml.Toml as WRAPPED

object Toml {
    fun asTree(raw: String): Tree.Node {
        return asTree(WRAPPED().read(raw))
    }

    private fun asTree(toml: WRAPPED): Tree.Node {
        val children = toml.entrySet().map {
            val value = it.value
            it.key to when (value) {
                is WRAPPED -> asTree(value)
                is List<*> -> asArray(value)
                else -> throw IllegalArgumentException("not implemented: ${value.javaClass}")
            }
        }.toMap()

        return Tree.Node(children)
    }

    private fun <T> asArray(value: List<T>): Tree.Array<T> {
        return Tree.Array(value)
    }
}