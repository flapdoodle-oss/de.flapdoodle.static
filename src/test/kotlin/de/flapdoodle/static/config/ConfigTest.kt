package de.flapdoodle.static.config

import de.flapdoodle.static.filetypes.Toml
import de.flapdoodle.static.io.readResource
import org.junit.jupiter.api.Test

private class ConfigTest {

    @Test
    fun readUsecase() {
        val configFile = javaClass.readResource("/usecase/sample.toml")
        val toml = Toml.asTree(configFile)
        val config = Config.parse(toml)
        println(config)
    }
}