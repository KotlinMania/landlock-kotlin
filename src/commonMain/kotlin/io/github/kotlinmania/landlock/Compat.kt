// port-lint: source landlock/src/compat.rs
package io.github.kotlinmania.landlock

/**
 * ABI version of the Landlock kernel subsystem.
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
            is NotEnabled, is NotImplemented -> ABI.Unsupported
            is Available -> effectiveAbi
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
enum class CompatLevel {
    BestEffort,
    SoftRequirement,
    HardRequirement,
    ;

    companion object {
        val DEFAULT = BestEffort
    }
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
        fun from(abi: ABI): Compatibility =
            Compatibility(
                status = if (abi == ABI.Unsupported) LandlockStatus.NotImplemented else LandlockStatus.Available(abi, abi.value),
                level = null,
                state = CompatState.Init,
            )
    }
}

/**
 * Types that support configuring Landlock compatibility requirement levels.
 */
interface Compatible {
    fun setCompatibility(level: CompatLevel): Compatible
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

fun canEmulate(abi: ABI, partial: ABI, full: ABI?): Boolean {
    val fullTarget = full ?: partial
    return abi.value >= partial.value || abi.value >= fullTarget.value
}
