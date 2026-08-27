// port-lint: tests landlock/src/uapi/landlock_all.rs
// Verifies architecture-specific UAPI binding facts.
package io.github.kotlinmania.landlock.uapi

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import io.github.kotlinmania.landlock.uapi.i686.LANDLOCK_ACCESS_FS_EXECUTE as I686_ACCESS_FS_EXECUTE
import io.github.kotlinmania.landlock.uapi.i686.LANDLOCK_ACCESS_NET_BIND_TCP as I686_ACCESS_NET_BIND_TCP
import io.github.kotlinmania.landlock.uapi.i686.LANDLOCK_NET_PORT_ATTR_ALIGNMENT_BYTES as I686_NET_PORT_ALIGNMENT
import io.github.kotlinmania.landlock.uapi.i686.LANDLOCK_NET_PORT_ATTR_PORT_OFFSET as I686_NET_PORT_PORT_OFFSET
import io.github.kotlinmania.landlock.uapi.i686.LANDLOCK_NET_PORT_ATTR_SIZE_BYTES as I686_NET_PORT_SIZE
import io.github.kotlinmania.landlock.uapi.i686.LANDLOCK_PATH_BENEATH_ATTR_ALIGNMENT_BYTES as I686_PATH_BENEATH_ALIGNMENT
import io.github.kotlinmania.landlock.uapi.i686.LANDLOCK_PATH_BENEATH_ATTR_PARENT_FD_OFFSET as I686_PATH_BENEATH_PARENT_FD_OFFSET
import io.github.kotlinmania.landlock.uapi.i686.LANDLOCK_PATH_BENEATH_ATTR_SIZE_BYTES as I686_PATH_BENEATH_SIZE
import io.github.kotlinmania.landlock.uapi.i686.LANDLOCK_RULESET_ATTR_ALIGNMENT_BYTES as I686_RULESET_ALIGNMENT
import io.github.kotlinmania.landlock.uapi.i686.LANDLOCK_RULESET_ATTR_SCOPED_OFFSET as I686_RULESET_SCOPED_OFFSET
import io.github.kotlinmania.landlock.uapi.i686.LANDLOCK_RULESET_ATTR_SIZE_BYTES as I686_RULESET_SIZE
import io.github.kotlinmania.landlock.uapi.i686.bindgenTestLayoutLandlockNetPortAttr as i686NetPortLayoutMatches
import io.github.kotlinmania.landlock.uapi.i686.bindgenTestLayoutLandlockPathBeneathAttr as i686PathBeneathLayoutMatches
import io.github.kotlinmania.landlock.uapi.i686.bindgenTestLayoutLandlockRulesetAttr as i686RulesetLayoutMatches
import io.github.kotlinmania.landlock.uapi.x8664.LANDLOCK_ACCESS_FS_EXECUTE as X8664_ACCESS_FS_EXECUTE
import io.github.kotlinmania.landlock.uapi.x8664.LANDLOCK_ACCESS_NET_BIND_TCP as X8664_ACCESS_NET_BIND_TCP
import io.github.kotlinmania.landlock.uapi.x8664.LANDLOCK_NET_PORT_ATTR_ALIGNMENT_BYTES as X8664_NET_PORT_ALIGNMENT
import io.github.kotlinmania.landlock.uapi.x8664.LANDLOCK_NET_PORT_ATTR_PORT_OFFSET as X8664_NET_PORT_PORT_OFFSET
import io.github.kotlinmania.landlock.uapi.x8664.LANDLOCK_NET_PORT_ATTR_SIZE_BYTES as X8664_NET_PORT_SIZE
import io.github.kotlinmania.landlock.uapi.x8664.LANDLOCK_PATH_BENEATH_ATTR_ALIGNMENT_BYTES as X8664_PATH_BENEATH_ALIGNMENT
import io.github.kotlinmania.landlock.uapi.x8664.LANDLOCK_PATH_BENEATH_ATTR_PARENT_FD_OFFSET as X8664_PATH_BENEATH_PARENT_FD_OFFSET
import io.github.kotlinmania.landlock.uapi.x8664.LANDLOCK_PATH_BENEATH_ATTR_SIZE_BYTES as X8664_PATH_BENEATH_SIZE
import io.github.kotlinmania.landlock.uapi.x8664.LANDLOCK_RULESET_ATTR_ALIGNMENT_BYTES as X8664_RULESET_ALIGNMENT
import io.github.kotlinmania.landlock.uapi.x8664.LANDLOCK_RULESET_ATTR_SCOPED_OFFSET as X8664_RULESET_SCOPED_OFFSET
import io.github.kotlinmania.landlock.uapi.x8664.LANDLOCK_RULESET_ATTR_SIZE_BYTES as X8664_RULESET_SIZE
import io.github.kotlinmania.landlock.uapi.x8664.bindgenTestLayoutLandlockNetPortAttr as x8664NetPortLayoutMatches
import io.github.kotlinmania.landlock.uapi.x8664.bindgenTestLayoutLandlockPathBeneathAttr as x8664PathBeneathLayoutMatches
import io.github.kotlinmania.landlock.uapi.x8664.bindgenTestLayoutLandlockRulesetAttr as x8664RulesetLayoutMatches

class LandlockArchitectureBindingsTest {
    @Test
    fun architectureConstantsMatchAgnosticBindings() {
        assertEquals(LANDLOCK_ACCESS_FS_EXECUTE, I686_ACCESS_FS_EXECUTE)
        assertEquals(LANDLOCK_ACCESS_NET_BIND_TCP, I686_ACCESS_NET_BIND_TCP)
        assertEquals(LANDLOCK_ACCESS_FS_EXECUTE, X8664_ACCESS_FS_EXECUTE)
        assertEquals(LANDLOCK_ACCESS_NET_BIND_TCP, X8664_ACCESS_NET_BIND_TCP)
    }

    @Test
    fun i686LayoutMetadataMatchesBindgenTests() {
        assertTrue(i686RulesetLayoutMatches())
        assertTrue(i686PathBeneathLayoutMatches())
        assertTrue(i686NetPortLayoutMatches())

        assertEquals(24, I686_RULESET_SIZE)
        assertEquals(4, I686_RULESET_ALIGNMENT)
        assertEquals(16, I686_RULESET_SCOPED_OFFSET)

        assertEquals(12, I686_PATH_BENEATH_SIZE)
        assertEquals(1, I686_PATH_BENEATH_ALIGNMENT)
        assertEquals(8, I686_PATH_BENEATH_PARENT_FD_OFFSET)

        assertEquals(16, I686_NET_PORT_SIZE)
        assertEquals(4, I686_NET_PORT_ALIGNMENT)
        assertEquals(8, I686_NET_PORT_PORT_OFFSET)
    }

    @Test
    fun x8664LayoutMetadataMatchesBindgenTests() {
        assertTrue(x8664RulesetLayoutMatches())
        assertTrue(x8664PathBeneathLayoutMatches())
        assertTrue(x8664NetPortLayoutMatches())

        assertEquals(24, X8664_RULESET_SIZE)
        assertEquals(8, X8664_RULESET_ALIGNMENT)
        assertEquals(16, X8664_RULESET_SCOPED_OFFSET)

        assertEquals(12, X8664_PATH_BENEATH_SIZE)
        assertEquals(1, X8664_PATH_BENEATH_ALIGNMENT)
        assertEquals(8, X8664_PATH_BENEATH_PARENT_FD_OFFSET)

        assertEquals(16, X8664_NET_PORT_SIZE)
        assertEquals(8, X8664_NET_PORT_ALIGNMENT)
        assertEquals(8, X8664_NET_PORT_PORT_OFFSET)
    }
}
