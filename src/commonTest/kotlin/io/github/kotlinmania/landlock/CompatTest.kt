// port-lint: tests compat.rs
package io.github.kotlinmania.landlock

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CompatTest {
    @Test
    fun testAbiFrom() {
        for (n in listOf(-95, -38, -1, 0)) {
            assertEquals(ABI.Unsupported, ABI.from(n))
        }

        var lastI = 0
        var lastAbi = ABI.Unsupported
        for ((i, abi) in ABI.iter().asSequence().withIndex()) {
            lastI = i
            lastAbi = abi
            assertEquals(lastAbi, ABI.from(lastI))
        }

        assertEquals(lastAbi, ABI.from(lastI + 1))
        assertEquals(lastAbi, ABI.from(999))
    }

    @Test
    fun testAbiIsKnown() {
        assertFalse(ABI.isKnown(-1))
        assertFalse(ABI.isKnown(0))
        assertFalse(ABI.isKnown(999))

        var lastI = -1
        for ((i, _) in ABI
            .iter()
            .asSequence()
            .drop(1)
            .withIndex()) {
            lastI = i + 1
            assertTrue(ABI.isKnown(lastI))
        }
        assertFalse(ABI.isKnown(lastI + 1))
    }

    @Test
    fun testCurrentLandlockStatus() {
        val status = LandlockStatus.current()
        assertEquals(LandlockStatus.NotImplemented, status)
    }

    @Test
    fun testCompatStateUpdate1() {
        var state = CompatState.Full

        state = state.update(CompatState.Full)
        assertEquals(CompatState.Full, state)

        state = state.update(CompatState.No)
        assertEquals(CompatState.Partial, state)

        state = state.update(CompatState.Full)
        assertEquals(CompatState.Partial, state)

        state = state.update(CompatState.Full)
        assertEquals(CompatState.Partial, state)

        state = state.update(CompatState.No)
        assertEquals(CompatState.Partial, state)

        state = state.update(CompatState.Dummy)
        assertEquals(CompatState.Dummy, state)

        state = state.update(CompatState.Full)
        assertEquals(CompatState.Dummy, state)
    }

    @Test
    fun testCompatStateUpdate2() {
        var state = CompatState.Full

        state = state.update(CompatState.Full)
        assertEquals(CompatState.Full, state)

        state = state.update(CompatState.No)
        assertEquals(CompatState.Partial, state)

        state = state.update(CompatState.Full)
        assertEquals(CompatState.Partial, state)
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

    @Test
    fun testDeprecatedSetBestEffort() {
        val r1 = Ruleset()
        r1.setBestEffort(true)
        val r2 = Ruleset()
        r2.setCompatibility(CompatLevel.BestEffort)
        assertEquals(r2.compat.level, r1.compat.level)

        val r3 = Ruleset()
        r3.setBestEffort(false)
        val r4 = Ruleset()
        r4.setCompatibility(CompatLevel.HardRequirement)
        assertEquals(r4.compat.level, r3.compat.level)
    }

    @Test
    fun testTailoredCompatLevel() {
        fun newPath(level: CompatLevel): PathBeneath =
            PathBeneath(PathFd("/"), BitFlags.from(AccessFs.Execute)).apply {
                setCompatibility(level)
            }

        for (parentLevel in CompatLevel.entries) {
            assertEquals(
                parentLevel,
                newPath(CompatLevel.BestEffort).tailoredCompatLevel(parentLevel),
            )
            assertEquals(
                CompatLevel.HardRequirement,
                newPath(CompatLevel.HardRequirement).tailoredCompatLevel(parentLevel),
            )
        }

        assertEquals(
            CompatLevel.SoftRequirement,
            newPath(CompatLevel.SoftRequirement).tailoredCompatLevel(CompatLevel.SoftRequirement),
        )

        for (childLevel in CompatLevel.entries) {
            assertEquals(
                childLevel,
                newPath(childLevel).tailoredCompatLevel(CompatLevel.BestEffort),
            )
            assertEquals(
                CompatLevel.HardRequirement,
                newPath(childLevel).tailoredCompatLevel(CompatLevel.HardRequirement),
            )
        }
    }
}
