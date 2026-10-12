// port-lint: tests access.rs
package io.github.kotlinmania.landlock

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class AccessTest {
    @Test
    fun testBitFlagsFullNegation() {
        val allFs = AccessFs.ALL
        val scopedNegation = allFs and fullNegation(allFs)
        assertEquals(BitFlags.empty(), scopedNegation)
        assertNotEquals(scopedNegation, fullNegation(allFs))
    }

    @Test
    fun testCompatBitFlags() {
        var compat = Compatibility.from(ABI.V1)
        assertEquals(CompatState.Init, compat.state)

        val roAccess = BitFlags.from(AccessFs.Execute, AccessFs.ReadFile, AccessFs.ReadDir)
        val res = roAccess.tryCompat(compat.abi(), compat.level, compat, AccessFs.ALL, AccessFs.fromAll(compat.abi()))
        assertTrue(res.isSuccess)
        assertEquals(roAccess, res.getOrThrow())
        assertEquals(CompatState.Full, compat.state)

        val emptyAccess = BitFlags.empty()
        val emptyRes = emptyAccess.tryCompat(compat.abi(), compat.level, compat, AccessFs.ALL, AccessFs.fromAll(compat.abi()))
        assertTrue(emptyRes.isFailure)
        assertTrue(emptyRes.exceptionOrNull() is CompatError.Access)
        assertEquals(AccessError.Empty, (emptyRes.exceptionOrNull() as CompatError.Access).error)

        val allUnknownAccess = BitFlags.fromBitsUnchecked(1uL shl 63)
        val allUnknownRes = allUnknownAccess.tryCompat(compat.abi(), compat.level, compat, AccessFs.ALL, AccessFs.fromAll(compat.abi()))
        assertTrue(allUnknownRes.isFailure)
        assertTrue(allUnknownRes.exceptionOrNull() is CompatError.Access)
        val unknownErr = (allUnknownRes.exceptionOrNull() as CompatError.Access).error
        assertTrue(unknownErr is AccessError.Unknown)
        assertEquals(allUnknownAccess, unknownErr.access)
        assertEquals(allUnknownAccess, unknownErr.unknown)
        assertEquals(CompatState.Dummy, compat.state)

        val someUnknownAccess = BitFlags.fromBitsUnchecked((1uL shl 63) or 1uL)
        val someUnknownRes = someUnknownAccess.tryCompat(compat.abi(), compat.level, compat, AccessFs.ALL, AccessFs.fromAll(compat.abi()))
        assertTrue(someUnknownRes.isFailure)
        assertEquals(CompatState.Dummy, compat.state)

        compat = Compatibility.from(ABI.Unsupported)
        assertEquals(CompatState.Init, compat.state)

        val unsupportedRes = roAccess.tryCompat(compat.abi(), compat.level, compat, AccessFs.ALL, AccessFs.fromAll(compat.abi()))
        assertTrue(unsupportedRes.isSuccess)
        assertEquals(null, unsupportedRes.getOrThrow())
        assertEquals(CompatState.No, compat.state)

        compat.level = CompatLevel.HardRequirement
        val hardReqRes = roAccess.tryCompat(compat.abi(), compat.level, compat, AccessFs.ALL, AccessFs.fromAll(compat.abi()))
        assertTrue(hardReqRes.isFailure)
        val incompErr = (hardReqRes.exceptionOrNull() as CompatError.Access).error
        assertTrue(incompErr is AccessError.Incompatible)
        assertEquals(roAccess, incompErr.access)

        compat = Compatibility.from(ABI.V1)
        assertEquals(CompatState.Init, compat.state)

        val v1Res = roAccess.tryCompat(compat.abi(), compat.level, compat, AccessFs.ALL, AccessFs.fromAll(compat.abi()))
        assertTrue(v1Res.isSuccess)
        assertEquals(roAccess, v1Res.getOrThrow())
        assertEquals(CompatState.Full, compat.state)

        val v2Access = roAccess or AccessFs.Refer
        compat.level = CompatLevel.HardRequirement
        val v2Res = v2Access.tryCompat(compat.abi(), compat.level, compat, AccessFs.ALL, AccessFs.fromAll(compat.abi()))
        assertTrue(v2Res.isFailure)
        val partErr = (v2Res.exceptionOrNull() as CompatError.Access).error
        assertTrue(partErr is AccessError.PartiallyCompatible)
        assertEquals(v2Access, partErr.access)
        assertEquals(BitFlags.from(AccessFs.Refer), partErr.incompatible)
    }
}
