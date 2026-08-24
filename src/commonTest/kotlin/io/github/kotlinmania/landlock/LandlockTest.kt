package io.github.kotlinmania.landlock

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LandlockTest {
    @Test
    fun testAbiVersions() {
        assertEquals(0, ABI.Unsupported.value)
        assertEquals(1, ABI.V1.value)
        assertEquals(2, ABI.V2.value)
        assertEquals(3, ABI.V3.value)
        assertEquals(4, ABI.V4.value)
        assertEquals(5, ABI.V5.value)
        assertEquals(6, ABI.V6.value)

        assertEquals(ABI.Unsupported, ABI.from(0))
        assertEquals(ABI.V1, ABI.from(1))
        assertEquals(ABI.V6, ABI.from(6))
        assertEquals(ABI.V6, ABI.from(7))

        assertTrue(ABI.isKnown(1))
        assertTrue(ABI.isKnown(6))
        assertFalse(ABI.isKnown(0))
        assertFalse(ABI.isKnown(7))
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
        assertTrue(BitFlags.empty<AccessFs>().isEmpty)

        val diff = combined and !exec
        assertEquals(read, diff)
    }

    @Test
    fun testRulesetHandleAccessFs() {
        val access = BitFlags.from(AccessFs.Execute, AccessFs.ReadDir)

        val ruleset =
            Ruleset
                .from(ABI.V1)
                .handleAccess(access)
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
                .handleAccess(access)
                .getOrThrow()
        assertEquals(access, rulesetV3.requestedHandledNet)
        assertEquals(BitFlags.empty(), rulesetV3.actualHandledNet)

        val rulesetV4 =
            Ruleset
                .from(ABI.V4)
                .handleAccess(access)
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
                .handleAccess(accessFs)
                .getOrThrow()
                .scope(scopes)
                .getOrThrow()
                .handleAccess(accessNet)
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
    fun testNetPortConsistencyCheck() {
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
        assertTrue(failure.exceptionOrNull() is RulesetError.AddRules)
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
    fun testTryClone() {
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
    fun testAllowRootCompat() {
        val abi = ABI.V1
        val status =
            Ruleset
                .from(abi)
                .handleAccess(AccessFs.fromAll(abi))
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
    fun testAllowRootFragile() {
        val abi = ABI.V1
        val status =
            Ruleset
                .from(abi)
                .setCompatibility(CompatLevel.HardRequirement)
                .handleAccess(AccessFs.Execute)
                .getOrThrow()
                .setCompatibility(CompatLevel.BestEffort)
                .handleAccess(AccessFs.fromAll(abi))
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
    fun testAbiV2ExecRefer() {
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
    fun testAbiV3Truncate() {
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
    fun testAbiV4Tcp() {
        val status =
            Ruleset
                .from(ABI.V4)
                .handleAccess(AccessFs.Truncate)
                .getOrThrow()
                .handleAccess(BitFlags.from(AccessNet.BindTcp, AccessNet.ConnectTcp))
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
    fun testAbiV5IoctlDev() {
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
    fun testAbiV6ScopeMix() {
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
}
