package de.flapdoodle.statik.path

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PathTest {
    @Test
    fun sample() {
        val path = Path.parse("/foo/bar/:property-nix/:other/nix")
        assertEquals(5, path.parts.size)
        assertEquals("/foo/bar/", (path.parts.get(0) as Path.Part.Static).fixed)
        assertEquals("property", (path.parts.get(1) as Path.Part.Property).property)
        assertEquals("-nix/", (path.parts.get(2) as Path.Part.Static).fixed)
        assertEquals("other", (path.parts.get(3) as Path.Part.Property).property)
        assertEquals("/nix", (path.parts.get(4) as Path.Part.Static).fixed)
    }

    @Test
    fun withFormatter() {
        val path = Path.parse("/foo/bar/:property#number-nix/:other/nix")
        assertEquals(5, path.parts.size)
        assertEquals("/foo/bar/", (path.parts.get(0) as Path.Part.Static).fixed)
        assertEquals("property", (path.parts.get(1) as Path.Part.Property).property)
        assertEquals("number", (path.parts.get(1) as Path.Part.Property).formatter)
        assertEquals("-nix/", (path.parts.get(2) as Path.Part.Static).fixed)
        assertEquals("other", (path.parts.get(3) as Path.Part.Property).property)
        assertFalse((path.parts.get(3) as Path.Part.Property).formatter!=null)
        assertEquals("/nix", (path.parts.get(4) as Path.Part.Static).fixed)
    }
}