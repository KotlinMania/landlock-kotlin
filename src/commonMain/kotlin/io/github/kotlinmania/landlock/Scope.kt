// port-lint: source scope.rs
package io.github.kotlinmania.landlock

import io.github.kotlinmania.landlock.uapi.LANDLOCK_SCOPE_ABSTRACT_UNIX_SOCKET
import io.github.kotlinmania.landlock.uapi.LANDLOCK_SCOPE_SIGNAL

/**
 * Landlock scope restriction flags.
 */
enum class Scope(
    override val bits: ULong,
) : Access {
    AbstractUnixSocket(LANDLOCK_SCOPE_ABSTRACT_UNIX_SOCKET.toULong()),
    Signal(LANDLOCK_SCOPE_SIGNAL.toULong()),
    ;

    companion object {
        val ALL: BitFlags<Scope> = BitFlags.from(AbstractUnixSocket, Signal)

        fun fromAll(abi: ABI): BitFlags<Scope> =
            when (abi) {
                ABI.Unsupported, ABI.V1, ABI.V2, ABI.V3, ABI.V4, ABI.V5 -> BitFlags.empty()
                ABI.V6 -> ALL
            }
    }
}
