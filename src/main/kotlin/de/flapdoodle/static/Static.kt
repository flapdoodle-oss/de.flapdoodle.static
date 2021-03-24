package de.flapdoodle.static

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.validate
import com.github.ajalt.clikt.parameters.types.path

object Static {

    class Args : CliktCommand() {
        init {
            context {
                allowInterspersedArgs = false
            }
        }
        val source by argument("source").path(
            mustExist = true,
            canBeFile = false,
            canBeDir = true
        ).validate {
            require(it.toFile().isDirectory) { "is not a directory"}
        }

        val destination by argument("destination").path(
            mustExist = true,
            canBeFile = false,
            canBeDir = true
        ).validate {
            require(it.toFile().isDirectory) { "is not a directory"}
        }

        override fun run() {
            TODO("Not yet implemented")
        }
    }
    
    @JvmStatic
    fun main(args: Array<String>) {
        Args().main(args.toList())
    }
}