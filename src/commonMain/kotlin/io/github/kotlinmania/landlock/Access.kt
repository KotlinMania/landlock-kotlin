// port-lint: source landlock/src/access.rs
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
 * A type-safe set of access-right flags.
 */
data class BitFlags(
    val bits: ULong = 0uL,
) {
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

    override fun toString(): String = "BitFlags(0x${bits.toString(16)})"

    companion object {
        fun empty(): BitFlags = BitFlags(0uL)

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
