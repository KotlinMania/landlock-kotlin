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
}
