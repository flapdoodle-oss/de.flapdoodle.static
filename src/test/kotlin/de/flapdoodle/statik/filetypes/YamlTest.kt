package de.flapdoodle.statik.filetypes

import de.flapdoodle.statik.io.readResource
import org.junit.jupiter.api.Test

private class YamlTest {

    @Test
    fun readSampleYaml() {
        val sample = javaClass.readResource("sample.yaml")
        val tree = Yaml.asTree(sample)

//        println("tree: $tree")
    }
}