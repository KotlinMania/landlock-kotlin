// port-lint: source net.rs
package io.github.kotlinmania.landlock

import io.github.kotlinmania.landlock.uapi.LANDLOCK_ACCESS_NET_BIND_TCP
import io.github.kotlinmania.landlock.uapi.LANDLOCK_ACCESS_NET_CONNECT_TCP
import io.github.kotlinmania.landlock.uapi.LandlockNetPortAttr
import io.github.kotlinmania.landlock.uapi.x8664.LANDLOCK_RULE_TYPE_LANDLOCK_RULE_NET_PORT
import io.github.kotlinmania.landlock.uapi.x8664.LandlockRuleType

/**
 * Network access rights for Landlock.
 */
enum class AccessNet(
    override val bits: ULong,
) : PrivateHandledAccess,
    Sealed {
    BindTcp(LANDLOCK_ACCESS_NET_BIND_TCP.toULong()),
    ConnectTcp(LANDLOCK_ACCESS_NET_CONNECT_TCP.toULong()),
    ;

    override fun rulesetHandleAccess(
        ruleset: Ruleset,
        access: BitFlags,
    ): Result<Unit> {
        val res = ruleset.handleAccessNet(access)
        return if (res.isSuccess) Result.success(Unit) else Result.failure(res.exceptionOrNull()!!)
    }

    override fun intoAddRulesError(error: AddRuleError): AddRulesError = AddRulesError.Net(error)

    override fun intoHandleAccessesError(error: HandleAccessError): HandleAccessesError = HandleAccessesError.Net(error)

    companion object {
        val ALL: BitFlags = BitFlags.from(BindTcp, ConnectTcp)

        fun all(): BitFlags = ALL

        fun fromAll(abi: ABI): BitFlags =
            when (abi) {
                ABI.Unsupported, ABI.V1, ABI.V2, ABI.V3 -> BitFlags.empty()
                ABI.V4, ABI.V5, ABI.V6 -> ALL
            }
    }
}

/**
 * Landlock rule for a network port.
 */
data class NetPort(
    val port: UShort,
    var allowedAccess: BitFlags,
    override var compatLevel: CompatLevel? = null,
) : Rule<AccessNet>,
    PrivateRule,
    Compatible {
    override val typeId: LandlockRuleType get() = LANDLOCK_RULE_TYPE_LANDLOCK_RULE_NET_PORT

    override fun asPtr(): Any? = toAttr()

    constructor(port: UShort, access: AccessNet) : this(port, BitFlags.from(access))

    constructor(port: UShort, access: BitFlags) : this(port, access, null)

    override fun asOptionCompatLevelMut(): CompatLevel? = compatLevel

    override fun setCompatibility(level: CompatLevel): NetPort {
        this.compatLevel = level
        return this
    }

    override fun checkConsistency(ruleset: RulesetCreated): Result<Unit> {
        if (ruleset.requestedHandledNet.contains(allowedAccess)) {
            return Result.success(Unit)
        }
        val incompatible = allowedAccess and !ruleset.requestedHandledNet
        return Result.failure(
            RulesetError.AddRules(
                AddRulesError.Net(
                    AddRuleError.UnhandledAccess(
                        access = allowedAccess,
                        incompatible = incompatible,
                    ),
                ),
            ),
        )
    }

    fun toAttr(): LandlockNetPortAttr =
        LandlockNetPortAttr(
            allowedAccess = allowedAccess.bits,
            port = port.toULong(),
        )
}
