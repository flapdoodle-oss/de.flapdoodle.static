package de.flapdoodle.statik.filetypes

import kotlin.reflect.KClass

sealed class Attributes {
    data class Node(val children: Map<String, Attributes>): Attributes() {
        fun keys(): Set<String> {
            return children.keys
        }

        fun nodeKeys(): Set<String> {
            return children
                .filter { it.value is Attributes.Node }
                .keys
        }

        fun <T: Attributes> find(key: String, type: KClass<T>): T? {
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

        fun <T: Attributes> get(key: String, type: KClass<T>): T {
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

        operator fun plus(other: Node): Node {
            val sameKeys = children.keys.intersect(other.keys())
            require(sameKeys.isEmpty()) {"key conflicts: $sameKeys"}
            return Node(children + other.children)
        }

        operator fun plus(value: Pair<String, Any>): Node {
            return this + Node(children = mapOf(value.first to Values(listOf(value.second))))
        }

        fun flatten(separator: String): Map<String, Any> {
            return flatten(null,separator)
        }

        private fun flatten(prefix: String?, separator: String): Map<String, Any> {
            var map= emptyMap<String, Any>()
            children.forEach { key, attributes ->
                when (attributes) {
                    is Node -> map = map + attributes
                        .flatten(prefix?.let { it+separator }?:key, separator)
                    is Values<*> -> {
                        if (attributes.values.size==1) {
                            // TODO how to handle this?
                            map = map + (key to attributes.values[0]!!)
                        }
                    }
                }
            }
            return map
        }
    }

    data class Values<T>(val values: List<T>): Attributes() {
        fun <X: Any> asListOf(valueType: KClass<X>): List<X> {
            if (values.all { valueType.isInstance(it) }) {
                return values as List<X>
            }
            throw IllegalArgumentException("value type mismatch: $values != $valueType")
        }
    }

    companion object {
        fun of(map: Map<String, Any>): Node {
            return Node(map.mapValues { Values(listOf(it.value)) })
        }
    }
}