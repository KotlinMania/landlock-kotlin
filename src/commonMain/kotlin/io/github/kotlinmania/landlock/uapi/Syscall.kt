package io.github.kotlinmania.landlock.uapi

import io.github.kotlinmania.landlock.uapi.x8664.LandlockRuleType
import io.github.kotlinmania.landlock.uapi.x8664.LandlockRulesetAttr
import io.github.kotlinmania.libc.unix.linuxlike.linux.gnu.b64.x8664.SYS_landlock_add_rule
import io.github.kotlinmania.libc.unix.linuxlike.linux.gnu.b64.x8664.SYS_landlock_create_ruleset
import io.github.kotlinmania.libc.unix.linuxlike.linux.gnu.b64.x8664.SYS_landlock_restrict_self

/**
 * Constants for the Landlock Linux system call numbers.
 */
public object LandlockSyscallNumber {
    public const val SYS_CREATE_RULESET: Long = SYS_landlock_create_ruleset
    public const val SYS_ADD_RULE: Long = SYS_landlock_add_rule
    public const val SYS_RESTRICT_SELF: Long = SYS_landlock_restrict_self
}

/**
 * Creates a new Landlock ruleset.
 */
public fun landlockCreateRuleset(
    attr: LandlockRulesetAttr?,
    size: ULong,
    flags: UInt,
): Int = -1

/**
 * Adds a rule to an existing Landlock ruleset.
 */
public fun landlockAddRule(
    rulesetFd: Int,
    ruleType: LandlockRuleType,
    ruleAttr: Any?,
    flags: UInt,
): Int = -1

/**
 * Restricts the calling thread with a Landlock ruleset.
 */
public fun landlockRestrictSelf(
    rulesetFd: Int,
    flags: UInt,
): Int = -1
