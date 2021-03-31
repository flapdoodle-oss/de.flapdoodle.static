package de.flapdoodle.statik.config

import de.flapdoodle.statik.filetypes.Attributes
import de.flapdoodle.statik.filetypes.Toml
import de.flapdoodle.statik.filetypes.Yaml
import java.nio.file.Files
import java.nio.file.Path

data class Config(
    val basePath: Path,
    val site: Site,
    val sources: Sources,
    val pages: Pages
) {

    companion object {
        fun parse(basePath: Path, source: Attributes.Node): Config {
            val siteConfig = source.get("site", Attributes.Node::class)
            val sourceConfig = source.get("sources", Attributes.Node::class)
            val pageConfig = source.get("pages", Attributes.Node::class)
            
            return Config(
                basePath = basePath,
                site = Site.parse(siteConfig),
                sources = Sources.parse(basePath, sourceConfig),
                pages = Pages.parse(pageConfig)
            )
        }

        fun parse(source: Path): Config {
            val content = Files.readString(source, Charsets.UTF_8)
            val fileName = source.fileName.toString()
            val configAsTree = when {
                fileName.endsWith(".toml") -> Toml.asTree(content)
                fileName.endsWith(".yaml") -> Yaml.asTree(content)
                else -> throw IllegalArgumentException("unsupported config file: $source")
            }
            return parse(source.parent, configAsTree)
        }
    }
}