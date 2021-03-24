package de.flapdoodle.static.filetypes

import de.flapdoodle.static.io.readResource
import org.junit.jupiter.api.Test

class TomlTest {

    @Test
    fun readSampleToml() {
        val sample = javaClass.readResource("sample.toml")
        val tree = Toml.asTree(sample)

        println("tree: $tree")
    }

}