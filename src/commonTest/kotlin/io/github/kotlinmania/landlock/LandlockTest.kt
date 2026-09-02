// port-lint: tests lib.rs
package io.github.kotlinmania.landlock

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LandlockTest {
    @Test
    fun abiFrom() {
        for (n in listOf(-95, -38, -1, 0)) {
            assertEquals(ABI.Unsupported, ABI.from(n))
        }

        var lastI = 1
        var lastAbi = ABI.Unsupported
        for ((i, abi) in ABI.entries.withIndex()) {
            lastI = i
            lastAbi = abi
            assertEquals(lastAbi, ABI.from(lastI))
        }

        assertEquals(lastAbi, ABI.from(lastI + 1))
        assertEquals(lastAbi, ABI.from(999))
    }

    @Test
    fun knownAbi() {
        assertFalse(ABI.isKnown(-1))
        assertFalse(ABI.isKnown(0))
        assertFalse(ABI.isKnown(999))

        var lastI = -1
        for ((i, _) in ABI.entries.withIndex().drop(1)) {
            lastI = i
            assertTrue(ABI.isKnown(lastI))
        }
        assertFalse(ABI.isKnown(lastI + 1))
    }

    @Test
    fun testBitFlagsOperations() {
        val exec = BitFlags.from(AccessFs.Execute)
        val read = BitFlags.from(AccessFs.ReadFile)
        val combined = exec or read

        assertTrue(combined.contains(AccessFs.Execute))
        assertTrue(combined.contains(AccessFs.ReadFile))
        assertFalse(combined.contains(AccessFs.WriteFile))
        assertTrue(combined.contains(exec))
        assertTrue(combined.contains(read))
        assertFalse(combined.isEmpty)
        assertTrue(BitFlags.empty().isEmpty)

        val diff = combined and !exec
        assertEquals(read, diff)
    }

    @Test
    fun consistentAccessFsRw() {
        for (abi in ABI.entries) {
            val accessAll = AccessFs.fromAll(abi)
            val accessRead = AccessFs.fromRead(abi)
            val accessWrite = AccessFs.fromWrite(abi)
            val accessFile = AccessFs.fromFile(abi)
            assertEquals(accessRead, !accessWrite and accessAll)
            assertEquals(accessAll, accessRead or accessWrite)
            assertEquals(accessFile, accessAll and AccessFs.fromFile(abi))
        }
    }

    @Test
    fun testRulesetHandleAccessFs() {
        val access = BitFlags.from(AccessFs.Execute, AccessFs.ReadDir)

        val ruleset =
            Ruleset
                .from(ABI.V1)
                .handleAccessFs(access)
                .getOrThrow()
        assertEquals(access, ruleset.requestedHandledFs)
        assertEquals(access, ruleset.actualHandledFs)

        val rulesetV2 =
            Ruleset
                .from(ABI.V1)
                .handleAccess(AccessFs.Execute)
                .getOrThrow()
                .handleAccess(AccessFs.ReadDir)
                .getOrThrow()
                .handleAccess(AccessFs.Execute)
                .getOrThrow()
        assertEquals(access, rulesetV2.requestedHandledFs)
        assertEquals(access, rulesetV2.actualHandledFs)
    }

    @Test
    fun testRulesetHandleAccessNetTcp() {
        val access = BitFlags.from(AccessNet.BindTcp, AccessNet.ConnectTcp)

        val rulesetV3 =
            Ruleset
                .from(ABI.V3)
                .handleAccessNet(access)
                .getOrThrow()
        assertEquals(access, rulesetV3.requestedHandledNet)
        assertEquals(BitFlags.empty(), rulesetV3.actualHandledNet)

        val rulesetV4 =
            Ruleset
                .from(ABI.V4)
                .handleAccessNet(access)
                .getOrThrow()
        assertEquals(access, rulesetV4.requestedHandledNet)
        assertEquals(access, rulesetV4.actualHandledNet)
    }

    @Test
    fun testRulesetScope() {
        val scopes = BitFlags.from(Scope.AbstractUnixSocket, Scope.Signal)

        val rulesetV5 =
            Ruleset
                .from(ABI.V5)
                .scope(scopes)
                .getOrThrow()
        assertEquals(scopes, rulesetV5.requestedScoped)
        assertEquals(BitFlags.empty(), rulesetV5.actualScoped)

        val rulesetV6 =
            Ruleset
                .from(ABI.V6)
                .scope(scopes)
                .getOrThrow()
        assertEquals(scopes, rulesetV6.requestedScoped)
        assertEquals(scopes, rulesetV6.actualScoped)
    }

    @Test
    fun testRulesetCreatedFsNetScope() {
        val accessFs = BitFlags.from(AccessFs.Execute, AccessFs.ReadDir)
        val accessNet = BitFlags.from(AccessNet.BindTcp, AccessNet.ConnectTcp)
        val scopes = BitFlags.from(Scope.AbstractUnixSocket, Scope.Signal)

        val ruleset =
            Ruleset
                .from(ABI.V6)
                .handleAccessFs(accessFs)
                .getOrThrow()
                .scope(scopes)
                .getOrThrow()
                .handleAccessNet(accessNet)
                .getOrThrow()

        assertEquals(accessFs, ruleset.requestedHandledFs)
        assertEquals(accessFs, ruleset.actualHandledFs)
        assertEquals(accessNet, ruleset.requestedHandledNet)
        assertEquals(accessNet, ruleset.actualHandledNet)
        assertEquals(scopes, ruleset.requestedScoped)
        assertEquals(scopes, ruleset.actualScoped)
    }

    @Test
    fun testRulesetRestrictSelf() {
        val status =
            Ruleset
                .from(ABI.V1)
                .handleAccess(AccessFs.Execute)
                .getOrThrow()
                .create()
                .getOrThrow()
                .restrictSelf()
                .getOrThrow()

        assertEquals(RulesetStatus.FullyEnforced, status.ruleset)
        assertTrue(status.noNewPrivs)
        assertEquals(ABI.V1, status.landlock.toAbi())
    }

    @Test
    fun netPortCheckConsistency() {
        val bind = AccessNet.BindTcp
        val bindConnect = BitFlags.from(bind, AccessNet.ConnectTcp)

        val created =
            Ruleset
                .from(ABI.Unsupported)
                .handleAccess(bind)
                .getOrThrow()
                .create()
                .getOrThrow()

        val failure = created.addRule(NetPort(1u, bindConnect))
        assertTrue(failure.isFailure)
        val err = failure.exceptionOrNull()
        assertTrue(err is RulesetError.AddRules)
        val cause = err.error
        assertTrue(cause is AddRulesError.Net)
        val ruleError = cause.error
        assertTrue(ruleError is AddRuleError.UnhandledAccess)
        assertEquals(bindConnect, ruleError.access)
        assertEquals(BitFlags.from(AccessNet.ConnectTcp), ruleError.incompatible)
    }

    @Test
    fun pathBeneathCheckConsistency() {
        val roAccess = BitFlags.from(AccessFs.ReadDir, AccessFs.ReadFile)
        val rxAccess = BitFlags.from(AccessFs.Execute, AccessFs.ReadFile)

        val err =
            Ruleset
                .from(ABI.Unsupported)
                .handleAccessFs(roAccess)
                .getOrThrow()
                .create()
                .getOrThrow()
                .addRule(PathBeneath(PathFd("/"), rxAccess))
                .exceptionOrNull()

        assertTrue(err is RulesetError.AddRules)
        val cause = err.error
        assertTrue(cause is AddRulesError.Fs)
        val ruleError = cause.error
        assertTrue(ruleError is AddRuleError.UnhandledAccess)
        assertEquals(rxAccess, ruleError.access)
        assertEquals(BitFlags.from(AccessFs.Execute), ruleError.incompatible)
    }

    @Test
    fun testRulesetAddRuleIter() {
        val failure =
            Ruleset
                .from(ABI.Unsupported)
                .handleAccess(AccessFs.Execute)
                .getOrThrow()
                .create()
                .getOrThrow()
                .addRule(PathBeneath("/", AccessFs.ReadFile))

        assertTrue(failure.isFailure)
        assertTrue(failure.exceptionOrNull() is RulesetError.AddRules)
    }

    @Test
    fun testPathBeneathRules() {
        val rules = pathBeneathRules(listOf("/usr", "/bin"), BitFlags.from(AccessFs.Execute))
        assertEquals(2, rules.size)
        assertTrue(rules.all { it.isSuccess })
    }

    @Test
    fun rulesetCreatedTryClone() {
        val ruleset1 =
            Ruleset
                .from(ABI.V1)
                .handleAccess(AccessFs.Execute)
                .getOrThrow()
                .create()
                .getOrThrow()

        val ruleset2 = ruleset1.tryClone().getOrThrow()
        val status = ruleset2.restrictSelf().getOrThrow()
        assertEquals(RulesetStatus.FullyEnforced, status.ruleset)
    }

    @Test
    fun allowRootCompat() {
        val abi = ABI.V1
        val status =
            Ruleset
                .from(abi)
                .handleAccessFs(AccessFs.fromAll(abi))
                .getOrThrow()
                .create()
                .getOrThrow()
                .addRule(PathBeneath(PathFd("/"), AccessFs.fromAll(abi)))
                .getOrThrow()
                .restrictSelf()
                .getOrThrow()
        assertEquals(RulesetStatus.FullyEnforced, status.ruleset)
    }

    @Test
    fun allowRootFragile() {
        val abi = ABI.V1
        val status =
            Ruleset
                .from(abi)
                .setCompatibility(CompatLevel.HardRequirement)
                .handleAccess(AccessFs.Execute)
                .getOrThrow()
                .setCompatibility(CompatLevel.BestEffort)
                .handleAccessFs(AccessFs.fromAll(abi))
                .getOrThrow()
                .create()
                .getOrThrow()
                .setNoNewPrivs(true)
                .addRule(PathBeneath(PathFd("/"), AccessFs.fromAll(abi)))
                .getOrThrow()
                .restrictSelf()
                .getOrThrow()
        assertEquals(RulesetStatus.FullyEnforced, status.ruleset)
        assertTrue(status.noNewPrivs)
    }

    @Test
    fun rulesetEnforced() {
        val status =
            Ruleset
                .from(ABI.V1)
                .handleAccess(AccessFs.Execute)
                .getOrThrow()
                .create()
                .getOrThrow()
                .restrictSelf()
                .getOrThrow()
        assertEquals(RulesetStatus.FullyEnforced, status.ruleset)
    }

    @Test
    fun abiV2ExecRefer() {
        val status =
            Ruleset
                .from(ABI.V2)
                .handleAccess(AccessFs.Execute)
                .getOrThrow()
                .handleAccess(AccessFs.Refer)
                .getOrThrow()
                .create()
                .getOrThrow()
                .restrictSelf()
                .getOrThrow()
        assertEquals(RulesetStatus.FullyEnforced, status.ruleset)
    }

    @Test
    fun abiV2ReferOnly() {
        val status =
            Ruleset
                .from(ABI.V2)
                .handleAccess(AccessFs.Refer)
                .getOrThrow()
                .create()
                .getOrThrow()
                .restrictSelf()
                .getOrThrow()
        assertEquals(RulesetStatus.FullyEnforced, status.ruleset)
    }

    @Test
    fun abiV3Truncate() {
        val status =
            Ruleset
                .from(ABI.V3)
                .handleAccess(AccessFs.Refer)
                .getOrThrow()
                .handleAccess(AccessFs.Truncate)
                .getOrThrow()
                .create()
                .getOrThrow()
                .addRule(PathBeneath(PathFd("/"), AccessFs.Refer))
                .getOrThrow()
                .restrictSelf()
                .getOrThrow()
        assertEquals(RulesetStatus.FullyEnforced, status.ruleset)
    }

    @Test
    fun abiV4Tcp() {
        val status =
            Ruleset
                .from(ABI.V4)
                .handleAccess(AccessFs.Truncate)
                .getOrThrow()
                .handleAccessNet(BitFlags.from(AccessNet.BindTcp, AccessNet.ConnectTcp))
                .getOrThrow()
                .create()
                .getOrThrow()
                .addRule(NetPort(1u, AccessNet.ConnectTcp))
                .getOrThrow()
                .restrictSelf()
                .getOrThrow()
        assertEquals(RulesetStatus.FullyEnforced, status.ruleset)
    }

    @Test
    fun abiV5IoctlDev() {
        val status =
            Ruleset
                .from(ABI.V5)
                .handleAccess(AccessNet.BindTcp)
                .getOrThrow()
                .handleAccess(AccessFs.IoctlDev)
                .getOrThrow()
                .create()
                .getOrThrow()
                .addRule(PathBeneath(PathFd("/"), AccessFs.IoctlDev))
                .getOrThrow()
                .restrictSelf()
                .getOrThrow()
        assertEquals(RulesetStatus.FullyEnforced, status.ruleset)
    }

    @Test
    fun abiV6ScopeMix() {
        val status =
            Ruleset
                .from(ABI.V6)
                .handleAccess(AccessFs.IoctlDev)
                .getOrThrow()
                .scope(BitFlags.from(Scope.AbstractUnixSocket, Scope.Signal))
                .getOrThrow()
                .create()
                .getOrThrow()
                .restrictSelf()
                .getOrThrow()
        assertEquals(RulesetStatus.FullyEnforced, status.ruleset)
    }

    @Test
    fun abiV6ScopeOnly() {
        val status =
            Ruleset
                .from(ABI.V6)
                .scope(BitFlags.from(Scope.AbstractUnixSocket, Scope.Signal))
                .getOrThrow()
                .create()
                .getOrThrow()
                .restrictSelf()
                .getOrThrow()
        assertEquals(RulesetStatus.FullyEnforced, status.ruleset)
    }
}
