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
 * A type-safe set of access-right flags.
 */
data class BitFlags<T : Access>(
    val bits: ULong = 0uL,
) {
    val isEmpty: Boolean get() = bits == 0uL
    val isNotEmpty: Boolean get() = bits != 0uL

    fun contains(flag: T): Boolean = (bits and flag.bits) == flag.bits

    fun contains(other: BitFlags<T>): Boolean = (bits and other.bits) == other.bits

    operator fun plus(flag: T): BitFlags<T> = BitFlags(bits or flag.bits)

    operator fun plus(other: BitFlags<T>): BitFlags<T> = BitFlags(bits or other.bits)

    infix fun or(flag: T): BitFlags<T> = BitFlags(bits or flag.bits)

    infix fun or(other: BitFlags<T>): BitFlags<T> = BitFlags(bits or other.bits)

    infix fun and(other: BitFlags<T>): BitFlags<T> = BitFlags(bits and other.bits)

    infix fun and(flag: T): BitFlags<T> = BitFlags(bits and flag.bits)

    infix fun xor(other: BitFlags<T>): BitFlags<T> = BitFlags(bits xor other.bits)

    operator fun not(): BitFlags<T> = BitFlags(bits.inv())

    override fun toString(): String = "BitFlags(0x${bits.toString(16)})"

    companion object {
        fun <T : Access> empty(): BitFlags<T> = BitFlags(0uL)

        fun <T : Access> from(vararg flags: T): BitFlags<T> {
            var mask = 0uL
            for (f in flags) mask = mask or f.bits
            return BitFlags(mask)
        }

        fun <T : Access> from(flags: Iterable<T>): BitFlags<T> {
            var mask = 0uL
            for (f in flags) mask = mask or f.bits
            return BitFlags(mask)
        }
    }
}
