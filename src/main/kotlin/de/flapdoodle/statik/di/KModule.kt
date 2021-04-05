package de.flapdoodle.statik.di

import com.google.inject.AbstractModule
import com.google.inject.binder.AnnotatedBindingBuilder
import com.google.inject.binder.ScopedBindingBuilder
import kotlin.reflect.KClass

fun <T: Any> AnnotatedBindingBuilder<T>.with(klazz: KClass<out T>): ScopedBindingBuilder {
    return this.to(klazz.java)
}

abstract class KModule : AbstractModule() {

    fun <T : Any> bind(klazz: KClass<T>): AnnotatedBindingBuilder<T> {
        return super.bind(klazz.java)
    }
}