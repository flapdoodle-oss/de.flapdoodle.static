package de.flapdoodle.statik.pipeline.generate

import de.flapdoodle.statik.path.Path
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class DefaultRenderPathTest {
    @Test
    fun pagePathShouldOmitFirstPage() {
        val renderer = DefaultRenderPath()
        val formatter = { property: String, formatterName: String? -> DefaultObjectFormatter() }
        val map = mapOf("bar" to "baz", "page" to 1)
        val result = renderer.render("base/", Path.parse("foo/:bar/:page/"), map::get, formatter)
        assertEquals("base/foo/baz/", result)
    }

    @Test
    fun doublePropertyShouldRender() {
        val renderer = DefaultRenderPath()
        val formatter = { property: String, formatterName: String? -> DefaultObjectFormatter() }
        val map = mapOf("bar" to 3.0, "page" to 1)
        val result = renderer.render("base/", Path.parse("foo/:bar/:page/"), map::get, formatter)
        assertEquals("base/foo/3.0/", result)
    }
}