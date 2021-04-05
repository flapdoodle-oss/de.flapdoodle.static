package de.flapdoodle.statik.di

import com.google.inject.Provider
import com.google.inject.Provides
import com.google.inject.Scopes
import de.flapdoodle.statik.pipeline.publish.Publisher
import de.flapdoodle.statik.pipeline.publish.UndertowPublisher
import javax.inject.Singleton

class PreviewPipelineModule : KModule() {
    override fun configure() {
        bind(Publisher::class).with(UndertowPublisher::class)
    }

    @Provides
    fun cleanUp(undertowPublisher: UndertowPublisher): CleanUp {
        return CleanUp {
            println("-----------------------------------------")
            println("server is running at http//localhost:8080")
            println("")
            println("press enter to stop")
            println("-----------------------------------------")
            System.`in`.read()
            println("stop...")
            undertowPublisher.stop()
        }
    }
}