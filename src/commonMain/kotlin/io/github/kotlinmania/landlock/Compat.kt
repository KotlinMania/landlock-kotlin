// port-lint: source compat.rs
package io.github.kotlinmania.landlock

/**
 * Version of the Landlock ABI.
 */
enum class ABI(
    val value: Int,
) {
    Unsupported(0),
    V1(1),
    V2(2),
    V3(3),
    V4(4),
    V5(5),
    V6(6),
    ;

    override fun toString(): String =
        when (this) {
            Unsupported -> "unsupported"
            else -> value.toString()
        }

    companion object {
        fun from(value: Int): ABI =
            when {
                value <= 0 -> Unsupported
                value == 1 -> V1
                value == 2 -> V2
                value == 3 -> V3
                value == 4 -> V4
                value == 5 -> V5
                else -> V6
            }

        fun isKnown(value: Int): Boolean = value in 1..6
    }
}

/**
 * Status of Landlock on the current platform or kernel.
 */
sealed class LandlockStatus {
    data object NotEnabled : LandlockStatus()

    data object NotImplemented : LandlockStatus()

    data class Available(
        val effectiveAbi: ABI,
        val kernelAbi: Int? = null,
    ) : LandlockStatus()

    fun toAbi(): ABI =
        when (this) {
            is Available -> effectiveAbi
            else -> ABI.Unsupported
        }

    companion object {
        internal val FALLBACK_STATUS: LandlockStatus = NotImplemented

        fun current(): LandlockStatus = FALLBACK_STATUS

        fun getErrnoFromLandlockStatus(): Int? = null
    }
}

/**
 * Internal state tracking compatibility across ruleset additions.
 */
enum class CompatState {
    Init,
    Full,
    Partial,
    No,
    Dummy,
    ;

    fun update(other: CompatState): CompatState =
        when {
            this == Init -> other
            this == Dummy || other == Dummy -> Dummy
            this == No && other == No -> No
            this == Full && other == Full -> Full
            else -> Partial
        }
}

/**
 * Requirement level for feature compatibility.
 */
enum class CompatLevel : Comparable<CompatLevel> {
    BestEffort,
    SoftRequirement,
    HardRequirement,
    ;

    companion object {
        val DEFAULT = BestEffort
    }
}

/**
 * Internal mutable capability for types carrying an optional CompatLevel.
 */
interface OptionCompatLevelMut {
    var compatLevel: CompatLevel?

    fun asOptionCompatLevelMut(): CompatLevel? = compatLevel
}

/**
 * Trait providing resolution of parent and child compatibility levels.
 */
interface TailoredCompatLevel {
    fun tailoredCompatLevel(parentLevel: CompatLevel): CompatLevel = parentLevel
}

/**
 * Types that support configuring Landlock compatibility requirement levels.
 */
interface Compatible :
    OptionCompatLevelMut,
    TailoredCompatLevel {
    fun setCompatibility(level: CompatLevel): Compatible {
        compatLevel = level
        return this
    }

    @Deprecated(
        "Use setCompatibility() instead",
        ReplaceWith("setCompatibility(if (bestEffort) CompatLevel.BestEffort else CompatLevel.HardRequirement)"),
    )
    fun setBestEffort(bestEffort: Boolean): Compatible =
        setCompatibility(if (bestEffort) CompatLevel.BestEffort else CompatLevel.HardRequirement)

    override fun tailoredCompatLevel(parentLevel: CompatLevel): CompatLevel {
        val level = asOptionCompatLevelMut()
        return if (level == null) parentLevel else maxOf(parentLevel, level)
    }
}

/**
 * Result of checking compatibility for access rights.
 */
sealed class CompatResult {
    data object Full : CompatResult()

    data class Partial(
        val error: CompatError,
    ) : CompatResult()

    data class No(
        val error: CompatError,
    ) : CompatResult()
}

/**
 * Compatibility configuration and state.
 */
data class Compatibility(
    var status: LandlockStatus = LandlockStatus.NotImplemented,
    var level: CompatLevel? = null,
    var state: CompatState = CompatState.Init,
) {
    fun update(newState: CompatState) {
        state = state.update(newState)
    }

    fun abi(): ABI = status.toAbi()

    companion object {
        fun new(): Compatibility =
            Compatibility(
                status = LandlockStatus.current(),
                level = null,
                state = CompatState.Init,
            )

        fun from(abi: ABI): Compatibility =
            Compatibility(
                status = if (abi == ABI.Unsupported) LandlockStatus.FALLBACK_STATUS else LandlockStatus.Available(abi, abi.value),
                level = null,
                state = CompatState.Init,
            )

        fun from(status: LandlockStatus): Compatibility =
            Compatibility(
                status = status,
                level = null,
                state = CompatState.Init,
            )
    }
}

/**
 * Trait for types supporting runtime Landlock compatibility transformations.
 */
interface TryCompat<T> : TailoredCompatLevel {
    fun tryCompatInner(abi: ABI): Result<CompatResult>

    fun tryCompatChildren(
        abi: ABI,
        parentLevel: CompatLevel,
        compatState: Compatibility,
    ): Result<T?> = Result.success(null)

    fun tryCompat(
        abi: ABI,
        parentLevel: CompatLevel,
        compatState: Compatibility,
    ): Result<T?>
}

fun canEmulate(abi: ABI, partial: ABI, full: ABI? = null): Boolean {
    val fullTarget = full ?: partial
    return abi.value >= partial.value || abi.value >= fullTarget.value
}
