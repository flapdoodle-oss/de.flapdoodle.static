package de.flapdoodle.statik

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.validate
import com.github.ajalt.clikt.parameters.types.path
import de.flapdoodle.statik.config.Config
import de.flapdoodle.statik.pipeline.Pipeline

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

        override fun run() {
            Pipeline().process(Config.parse(config))
        }
    }
    
    @JvmStatic
    fun main(args: Array<String>) {
        Args().main(args.toList())
    }
}