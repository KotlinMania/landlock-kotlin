// port-lint: tests errors.rs
package io.github.kotlinmania.landlock

import kotlin.test.Test
import kotlin.test.assertEquals

class ErrorsTest {
    @Test
    fun testErrnoConstantsAndFrom() {
        assertEquals(22, Errno.EINVAL)
        assertEquals(13, Errno.EACCES)
        assertEquals(5, Errno.EIO)
        assertEquals(38, Errno.ENOSYS)
        assertEquals(95, Errno.EOPNOTSUPP)
        assertEquals(7, Errno.E2BIG)

        val createCall = CreateRulesetError.CreateRulesetCall(22)
        assertEquals(Errno(22), Errno.from(createCall))

        val addRuleCall = AddRuleError.AddRuleCall(13)
        assertEquals(Errno(13), Errno.from(addRuleCall))

        val setPrivsCall = RestrictSelfError.SetNoNewPrivsCall(5)
        assertEquals(Errno(5), Errno.from(setPrivsCall))

        val restrictCall = RestrictSelfError.RestrictSelfCall(38)
        assertEquals(Errno(38), Errno.from(restrictCall))

        val openCall = PathFdError.OpenCall("/tmp", 95)
        assertEquals(Errno(95), Errno.from(openCall))

        val statCall = PathBeneathError.StatCall(7)
        assertEquals(Errno(7), Errno.from(statCall))
    }

    @Test
    fun testAccessErrors() {
        assertEquals("empty access-right", AccessError.Empty.message)
        assertEquals("missing access", CreateRulesetError.MissingHandledAccess.message)
    }

    @Test
    fun testRulesetErrorBreakingChange() {
        val err: RulesetError =
            RulesetError.HandleAccesses(
                HandleAccessesError.Fs(
                    HandleAccessError.Compat(
                        CompatError.Access(AccessError.Empty),
                    ),
                ),
            )
        assertEquals("empty access-right", err.message)
    }

    private fun testRulesetErrnoHelper(expectedErrno: Int) {
        val handleAccessErr =
            RulesetError.HandleAccesses(
                HandleAccessesError.Fs(
                    HandleAccessError.Compat(CompatError.Access(AccessError.Empty)),
                ),
            )
        assertEquals(Errno.EINVAL, Errno.from(handleAccessErr).value)

        val createRulesetErr =
            RulesetError.CreateRuleset(
                CreateRulesetError.CreateRulesetCall(expectedErrno),
            )
        assertEquals(expectedErrno, Errno.from(createRulesetErr).value)

        val addRulesFsErr =
            RulesetError.AddRules(
                AddRulesError.Fs(AddRuleError.AddRuleCall(expectedErrno)),
            )
        assertEquals(expectedErrno, Errno.from(addRulesFsErr).value)

        val addRulesNetErr =
            RulesetError.AddRules(
                AddRulesError.Net(AddRuleError.AddRuleCall(expectedErrno)),
            )
        assertEquals(expectedErrno, Errno.from(addRulesNetErr).value)

        val addRulesOtherErr =
            RulesetError.AddRules(
                AddRulesError.Fs(
                    AddRuleError.UnhandledAccess(
                        access = BitFlags.from(AccessFs.Execute),
                        incompatible = BitFlags.empty(),
                    ),
                ),
            )
        assertEquals(Errno.EINVAL, Errno.from(addRulesOtherErr).value)

        val restrictSelfErr =
            RulesetError.RestrictSelf(
                RestrictSelfError.RestrictSelfCall(expectedErrno),
            )
        assertEquals(expectedErrno, Errno.from(restrictSelfErr).value)

        val setNoNewPrivsErr =
            RulesetError.RestrictSelf(
                RestrictSelfError.SetNoNewPrivsCall(expectedErrno),
            )
        assertEquals(expectedErrno, Errno.from(setNoNewPrivsErr).value)

        val createRulesetMissingErr =
            RulesetError.CreateRuleset(
                CreateRulesetError.MissingHandledAccess,
            )
        assertEquals(Errno.EINVAL, Errno.from(createRulesetMissingErr).value)
    }

    @Test
    fun testRulesetErrno() {
        testRulesetErrnoHelper(Errno.EACCES)
        testRulesetErrnoHelper(Errno.EIO)
    }
}
