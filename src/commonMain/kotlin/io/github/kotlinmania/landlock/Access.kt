// port-lint: source access.rs
package io.github.kotlinmania.landlock

/**
 * Access represents an access-right bit flag.
 */
interface Access {
    val bits: ULong
}

/**
 * HandledAccess represents an access right that can be handled by a Landlock ruleset.
 */
interface HandledAccess : Access

/**
 * Internal interface for handled access rights with ruleset dispatch.
 */
interface PrivateHandledAccess : HandledAccess {
    fun rulesetHandleAccess(
        ruleset: Ruleset,
        access: BitFlags,
    ): Result<Unit>

    fun intoAddRulesError(error: AddRuleError): AddRulesError

    fun intoHandleAccessesError(error: HandleAccessError): HandleAccessesError
}

/**
 * Creates an inverted BitFlags instance with all bits toggled, including undefined ones.
 */
fun fullNegation(flags: BitFlags): BitFlags = BitFlags(flags.bits.inv())

/**
 * A type-safe set of access-right flags.
 */
data class BitFlags(
    val bits: ULong = 0uL,
) : TailoredCompatLevel {
    val isEmpty: Boolean get() = bits == 0uL
    val isNotEmpty: Boolean get() = bits != 0uL

    fun contains(flag: Access): Boolean = (bits and flag.bits) == flag.bits

    fun contains(other: BitFlags): Boolean = (bits and other.bits) == other.bits

    operator fun plus(flag: Access): BitFlags = BitFlags(bits or flag.bits)

    operator fun plus(other: BitFlags): BitFlags = BitFlags(bits or other.bits)

    infix fun or(flag: Access): BitFlags = BitFlags(bits or flag.bits)

    infix fun or(other: BitFlags): BitFlags = BitFlags(bits or other.bits)

    infix fun and(other: BitFlags): BitFlags = BitFlags(bits and other.bits)

    infix fun and(flag: Access): BitFlags = BitFlags(bits and flag.bits)

    infix fun xor(other: BitFlags): BitFlags = BitFlags(bits xor other.bits)

    operator fun not(): BitFlags = BitFlags(bits.inv())

    fun fullNegation(): BitFlags = fullNegation(this)

    fun tryCompatInner(
        abi: ABI,
        allSupported: BitFlags,
        fromAllAbi: BitFlags,
    ): Pair<BitFlags, Result<CompatResult>> {
        if (isEmpty) {
            return Pair(this, Result.failure(CompatError.Access(AccessError.Empty)))
        }
        if (!allSupported.contains(this)) {
            val unknown = this and fullNegation(allSupported)
            return Pair(this, Result.failure(CompatError.Access(AccessError.Unknown(this, unknown))))
        }
        val compat = this and fromAllAbi
        val res =
            if (compat.isEmpty) {
                CompatResult.No(CompatError.Access(AccessError.Incompatible(this)))
            } else if (compat != this) {
                val incompatible = this and fullNegation(compat)
                CompatResult.Partial(CompatError.Access(AccessError.PartiallyCompatible(this, incompatible)))
            } else {
                CompatResult.Full
            }
        return Pair(compat, Result.success(res))
    }

    fun tryCompat(
        abi: ABI,
        level: CompatLevel?,
        compatState: Compatibility,
        allSupported: BitFlags,
        fromAllAbi: BitFlags,
    ): Result<BitFlags?> {
        val compatLevel = level ?: CompatLevel.BestEffort
        val (compat, innerRes) = tryCompatInner(abi, allSupported, fromAllAbi)
        if (innerRes.isFailure) {
            compatState.update(CompatState.Dummy)
            return Result.failure(innerRes.exceptionOrNull()!!)
        }
        val someInner =
            when (val res = innerRes.getOrThrow()) {
                is CompatResult.Full -> {
                    compatState.update(CompatState.Full)
                    true
                }
                is CompatResult.Partial ->
                    when (compatLevel) {
                        CompatLevel.BestEffort -> {
                            compatState.update(CompatState.Partial)
                            true
                        }
                        CompatLevel.SoftRequirement -> {
                            compatState.update(CompatState.Dummy)
                            false
                        }
                        CompatLevel.HardRequirement -> {
                            compatState.update(CompatState.Dummy)
                            return Result.failure(res.error)
                        }
                    }
                is CompatResult.No ->
                    when (compatLevel) {
                        CompatLevel.BestEffort -> {
                            compatState.update(CompatState.No)
                            false
                        }
                        CompatLevel.SoftRequirement -> {
                            compatState.update(CompatState.Dummy)
                            false
                        }
                        CompatLevel.HardRequirement -> {
                            compatState.update(CompatState.Dummy)
                            return Result.failure(res.error)
                        }
                    }
            }
        return if (someInner) Result.success(compat) else Result.success(null)
    }

    override fun toString(): String = "BitFlags(0x${bits.toString(16)})"

    companion object {
        val EMPTY: BitFlags = BitFlags(0uL)

        fun empty(): BitFlags = EMPTY

        fun fromBits(bits: ULong): BitFlags = BitFlags(bits)

        fun fromBitsUnchecked(bits: ULong): BitFlags = BitFlags(bits)

        fun from(vararg flags: Access): BitFlags {
            var mask = 0uL
            for (f in flags) mask = mask or f.bits
            return BitFlags(mask)
        }

        fun from(flags: Iterable<Access>): BitFlags {
            var mask = 0uL
            for (f in flags) mask = mask or f.bits
            return BitFlags(mask)
        }
    }
}
