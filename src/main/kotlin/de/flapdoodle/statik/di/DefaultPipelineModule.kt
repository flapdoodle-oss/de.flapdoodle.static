package de.flapdoodle.statik.di

import com.google.inject.Provides
import de.flapdoodle.statik.pipeline.publish.Dump2ConsolePublisher
import de.flapdoodle.statik.pipeline.publish.Publisher

class DefaultPipelineModule : KModule() {

    override fun configure() {
        bind(Publisher::class).with(Dump2ConsolePublisher::class)
    }

    @Provides
    fun noopCleanup(): CleanUp {
        return CleanUp {  }
    }
}