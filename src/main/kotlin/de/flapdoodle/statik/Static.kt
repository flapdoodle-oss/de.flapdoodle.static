package de.flapdoodle.statik

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.validate
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import com.google.inject.Guice
import de.flapdoodle.statik.config.Config
import de.flapdoodle.statik.di.CleanUp
import de.flapdoodle.statik.di.PipelineModule
import de.flapdoodle.statik.pipeline.Pipeline
import de.flapdoodle.statik.pipeline.publish.Dump2ConsolePublisher
import de.flapdoodle.statik.pipeline.publish.Publisher
import de.flapdoodle.statik.pipeline.publish.UndertowPublisher
import java.io.Console
import java.util.*

object Static {

    class Args : CliktCommand() {
        init {
            context {
                allowInterspersedArgs = false
            }
        }
        val config by argument("config").path(
            mustExist = true,
            canBeFile = true,
            canBeDir = false
        ).validate {
            require(it.toFile().isFile) { "is not a file"}
        }

        val destination by argument("destination").path(
            mustExist = true,
            canBeFile = false,
            canBeDir = true
        ).validate {
            require(it.toFile().isDirectory) { "is not a directory"}
        }

        val preview by option("-p","--preview",help = "preview")
            .flag(default = false)

        override fun run() {
            val injector = Guice.createInjector(PipelineModule(true))
            val pipeline = injector.getInstance(Pipeline::class.java)
            val cleanup = injector.getInstance(CleanUp::class.java)
            
            pipeline.process(Config.parse(config))

            cleanup.doIt()
        }

        private fun publisher(preview: Boolean): Pair<Publisher, () -> Unit> {
            return if (preview) {
                val undertowPublisher = UndertowPublisher()
                undertowPublisher to { ->
                    println("-----------------------------------------")
                    println("server is running at http//localhost:8080")
                    println("")
                    println("press enter to stop")
                    println("-----------------------------------------")
                    System.`in`.read()
                    println("stop...")
                    undertowPublisher.stop()
                }
            } else {
                Dump2ConsolePublisher() to {}
            }
        }
    }
    
    @JvmStatic
    fun main(args: Array<String>) {
        Args().main(args.toList())
    }
}