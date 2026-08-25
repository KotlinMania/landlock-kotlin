// port-lint: tests compat.rs
package io.github.kotlinmania.landlock

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CompatTest {
    @Test
    fun testAbiFrom() {
        assertEquals(ABI.Unsupported, ABI.from(0))
        assertEquals(ABI.Unsupported, ABI.from(-1))
        assertEquals(ABI.V1, ABI.from(1))
        assertEquals(ABI.V2, ABI.from(2))
        assertEquals(ABI.V3, ABI.from(3))
        assertEquals(ABI.V4, ABI.from(4))
        assertEquals(ABI.V5, ABI.from(5))
        assertEquals(ABI.V6, ABI.from(6))
        assertEquals(ABI.V6, ABI.from(7))
    }

    @Test
    fun testAbiIsKnown() {
        assertFalse(ABI.isKnown(0))
        assertFalse(ABI.isKnown(-1))
        assertTrue(ABI.isKnown(1))
        assertTrue(ABI.isKnown(2))
        assertTrue(ABI.isKnown(3))
        assertTrue(ABI.isKnown(4))
        assertTrue(ABI.isKnown(5))
        assertTrue(ABI.isKnown(6))
        assertFalse(ABI.isKnown(7))
    }

    @Test
    fun testCompatStateUpdate() {
        assertEquals(CompatState.Full, CompatState.Init.update(CompatState.Full))
        assertEquals(CompatState.Partial, CompatState.Init.update(CompatState.Partial))
        assertEquals(CompatState.Dummy, CompatState.Full.update(CompatState.Dummy))
        assertEquals(CompatState.Dummy, CompatState.Dummy.update(CompatState.Full))
        assertEquals(CompatState.Full, CompatState.Full.update(CompatState.Full))
        assertEquals(CompatState.Partial, CompatState.Full.update(CompatState.Partial))
        assertEquals(CompatState.No, CompatState.No.update(CompatState.No))
    }

    @Test
    fun testCanEmulate() {
        assertTrue(canEmulate(ABI.V2, ABI.V1, ABI.V2))
        assertTrue(canEmulate(ABI.V1, ABI.V1, null))
        assertFalse(canEmulate(ABI.Unsupported, ABI.V1, ABI.V2))
    }

    @Test
    fun testCompatibilityState() {
        val compat = Compatibility.from(ABI.V3)
        assertEquals(ABI.V3, compat.abi())
        assertEquals(CompatState.Init, compat.state)

        compat.update(CompatState.Full)
        assertEquals(CompatState.Full, compat.state)

        compat.update(CompatState.Partial)
        assertEquals(CompatState.Partial, compat.state)
    }

    @Test
    fun testRulesetStatusFromState() {
        assertEquals(RulesetStatus.NotEnforced, RulesetStatus.from(CompatState.Init))
        assertEquals(RulesetStatus.NotEnforced, RulesetStatus.from(CompatState.No))
        assertEquals(RulesetStatus.NotEnforced, RulesetStatus.from(CompatState.Dummy))
        assertEquals(RulesetStatus.FullyEnforced, RulesetStatus.from(CompatState.Full))
        assertEquals(RulesetStatus.PartiallyEnforced, RulesetStatus.from(CompatState.Partial))
    }
}
