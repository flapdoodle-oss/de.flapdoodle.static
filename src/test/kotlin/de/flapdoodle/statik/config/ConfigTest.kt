package de.flapdoodle.statik.config

import de.flapdoodle.statik.filetypes.Toml
import de.flapdoodle.statik.io.readResource
import org.junit.jupiter.api.Test
import java.nio.file.Path

private class ConfigTest {

    @Test
    fun readUsecase() {
        val configFile = javaClass.readResource("/usecase/sample.toml")
        val toml = Toml.asTree(configFile)
        val config = Config.parse(Path.of("/basePath"),toml)
        println(config)
    }
}