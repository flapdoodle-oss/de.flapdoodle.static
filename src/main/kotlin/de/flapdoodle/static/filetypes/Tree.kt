package de.flapdoodle.static.filetypes

import kotlin.reflect.KClass
import kotlin.reflect.cast
import kotlin.reflect.safeCast

sealed class Tree {
    data class Node(val children: Map<String, Tree>): Tree() {
        fun keys(): Set<String> {
            return children.keys
        }

        fun <T: Tree> find(key: String, type: KClass<T>): T? {
            val value = children[key]
            if (value!=null) {
                if (type.isInstance(value)) {
                    return type.java.cast(value)
                } else {
                    throw IllegalArgumentException("type mismatch: $value != $type")
                }
            }
            return null
        }

        fun <T: Tree> get(key: String, type: KClass<T>): T {
            return find(key, type) ?: throw IllegalArgumentException("not found: $key")
        }

        fun <T: Any> values(key: String, type: KClass<T>): List<T> {
            return get(key,Values::class).asListOf(type)
        }

        fun <T: Any> findValues(key: String, type: KClass<T>): List<T>? {
            return find(key, Values::class)?.let {
                it.asListOf(type)
            }
        }
    }

    data class Values<T>(val values: List<T>): Tree() {
        fun <X: Any> asListOf(valueType: KClass<X>): List<X> {
            if (values.all { valueType.isInstance(it) }) {
                return values as List<X>
            }
            throw IllegalArgumentException("value type mismatch: $values != $valueType")
        }
    }
}