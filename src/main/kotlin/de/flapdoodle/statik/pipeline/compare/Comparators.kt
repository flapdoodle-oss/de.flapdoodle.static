package de.flapdoodle.statik.pipeline.compare

import de.flapdoodle.statik.types.head
import de.flapdoodle.statik.types.tail
import kotlin.reflect.KClass

fun <T: Any> Comparator<T>.themComparing(others: List<Comparator<in T>>):  Comparator<T> {
    var current = this
    others.forEach {
        current = current.thenComparing(it)
    }
    return current;
}

object Comparators {
    fun <T: Any, X: Any> compareWith(delegate: Comparator<X>, extract: (T) -> X): Comparator<T> {
        return Comparator { t1, t2 ->
            delegate.compare(extract(t1), extract(t2))
        }
    }

    fun <T: Any> guarded(delegate: Comparator<T>, type: KClass<out T>): GuardedComparator<T> {
        return GuardedComparator(delegate,type)
    }

    data class GuardedComparator<T: Any>(private val delegate: Comparator<T>, val type: KClass<out T>) : Comparator<T> {
        override fun compare(t1: T?, t2: T?): Int {
            require(t1==null  || type.isInstance(t1)) {"type mismatch: $t1 != $type"}
            require(t2==null  || type.isInstance(t2)) {"type mismatch: $t2 != $type"}

            return delegate.compare(t1,t2)
        }
    }
}