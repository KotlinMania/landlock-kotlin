// port-lint: source net.rs
package io.github.kotlinmania.landlock

import io.github.kotlinmania.landlock.uapi.LANDLOCK_ACCESS_NET_BIND_TCP
import io.github.kotlinmania.landlock.uapi.LANDLOCK_ACCESS_NET_CONNECT_TCP
import io.github.kotlinmania.landlock.uapi.LandlockNetPortAttr

/**
 * Network access rights for Landlock.
 */
enum class AccessNet(
    override val bits: ULong,
) : HandledAccess {
    BindTcp(LANDLOCK_ACCESS_NET_BIND_TCP.toULong()),
    ConnectTcp(LANDLOCK_ACCESS_NET_CONNECT_TCP.toULong()),
    ;

    companion object {
        val ALL: BitFlags<AccessNet> = BitFlags.from(BindTcp, ConnectTcp)

        fun fromAll(abi: ABI): BitFlags<AccessNet> =
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
    var allowedAccess: BitFlags<AccessNet>,
    var compatLevel: CompatLevel? = null,
) : Rule<AccessNet>,
    Compatible {
    constructor(port: UShort, access: AccessNet) : this(port, BitFlags.from(access))

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
