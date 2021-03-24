package de.flapdoodle.static.filetypes

import kotlin.reflect.KClass
import kotlin.reflect.cast

sealed class Tree {
    data class Node(val children: Map<String, Tree>): Tree() {
        fun keys(): Set<String> {
            return children.keys
        }

        fun <T: Tree> get(key: String, type: KClass<T>): T {
            val value = children[key]
            if (type.isInstance(value)) {
                return type.cast(value)
            }
            throw IllegalArgumentException("type mismatch: $value != $type")
        }
    }

    data class Array<T>(val values: List<T>): Tree() {
        fun <X: Any> asListOf(valueType: KClass<X>): List<X> {
            if (values.all { valueType.isInstance(it) }) {
                return values as List<X>
            }
            throw IllegalArgumentException("value type mismatch: $values != $valueType")
        }
    }
}