package de.flapdoodle.statik.filetypes

import de.flapdoodle.statik.io.readResource
import org.junit.jupiter.api.Test

class TomlTest {

    @Test
    fun readSampleToml() {
        val sample = javaClass.readResource("sample.toml")
        val tree = Toml.asTree(sample)

//        println("tree: $tree")
    }

}