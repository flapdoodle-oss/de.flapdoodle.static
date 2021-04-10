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
import de.flapdoodle.statik.io.Deferer
import de.flapdoodle.statik.io.PathWatcher
import de.flapdoodle.statik.pipeline.Pipeline
import de.flapdoodle.statik.pipeline.ProcessPipelineException
import de.flapdoodle.statik.pipeline.publish.Dump2ConsolePublisher
import de.flapdoodle.statik.pipeline.publish.Publisher
import de.flapdoodle.statik.pipeline.publish.UndertowPublisher
import java.io.Console
import java.lang.RuntimeException
import java.util.*
import java.util.concurrent.TimeUnit

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
            val injector = Guice.createInjector(PipelineModule(preview, destination))
            val pipeline = injector.getInstance(Pipeline::class.java)
            val cleanup = injector.getInstance(CleanUp::class.java)


            processPipeline(pipeline)

            if (preview) {
                val deferedPipeline = Deferer.onInactivityFor<Unit>(100,TimeUnit.MILLISECONDS) {
                    processPipeline(pipeline)
                }

                val watchDir = config.parent

                PathWatcher.watch(watchDir,100,TimeUnit.MILLISECONDS) {
                    deferedPipeline(Unit)
                }
            }

            cleanup.doIt()
        }

        private fun processPipeline(pipeline: Pipeline) {
            try {
                pipeline.process(Config.parse(config))
            } catch (ex: ProcessPipelineException) {
                ex.printStackTrace()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        Args().main(args.toList())
    }
}