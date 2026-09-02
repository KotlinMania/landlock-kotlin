// port-lint: source fs.rs
package io.github.kotlinmania.landlock

import io.github.kotlinmania.landlock.uapi.LANDLOCK_ACCESS_FS_EXECUTE
import io.github.kotlinmania.landlock.uapi.LANDLOCK_ACCESS_FS_IOCTL_DEV
import io.github.kotlinmania.landlock.uapi.LANDLOCK_ACCESS_FS_MAKE_BLOCK
import io.github.kotlinmania.landlock.uapi.LANDLOCK_ACCESS_FS_MAKE_CHAR
import io.github.kotlinmania.landlock.uapi.LANDLOCK_ACCESS_FS_MAKE_DIR
import io.github.kotlinmania.landlock.uapi.LANDLOCK_ACCESS_FS_MAKE_FIFO
import io.github.kotlinmania.landlock.uapi.LANDLOCK_ACCESS_FS_MAKE_REG
import io.github.kotlinmania.landlock.uapi.LANDLOCK_ACCESS_FS_MAKE_SOCK
import io.github.kotlinmania.landlock.uapi.LANDLOCK_ACCESS_FS_MAKE_SYM
import io.github.kotlinmania.landlock.uapi.LANDLOCK_ACCESS_FS_READ_DIR
import io.github.kotlinmania.landlock.uapi.LANDLOCK_ACCESS_FS_READ_FILE
import io.github.kotlinmania.landlock.uapi.LANDLOCK_ACCESS_FS_REFER
import io.github.kotlinmania.landlock.uapi.LANDLOCK_ACCESS_FS_REMOVE_DIR
import io.github.kotlinmania.landlock.uapi.LANDLOCK_ACCESS_FS_REMOVE_FILE
import io.github.kotlinmania.landlock.uapi.LANDLOCK_ACCESS_FS_TRUNCATE
import io.github.kotlinmania.landlock.uapi.LANDLOCK_ACCESS_FS_WRITE_FILE
import io.github.kotlinmania.landlock.uapi.LandlockPathBeneathAttr

/**
 * File system access rights for Landlock.
 */
enum class AccessFs(
    override val bits: ULong,
) : HandledAccess {
    Execute(LANDLOCK_ACCESS_FS_EXECUTE.toULong()),
    WriteFile(LANDLOCK_ACCESS_FS_WRITE_FILE.toULong()),
    ReadFile(LANDLOCK_ACCESS_FS_READ_FILE.toULong()),
    ReadDir(LANDLOCK_ACCESS_FS_READ_DIR.toULong()),
    RemoveDir(LANDLOCK_ACCESS_FS_REMOVE_DIR.toULong()),
    RemoveFile(LANDLOCK_ACCESS_FS_REMOVE_FILE.toULong()),
    MakeChar(LANDLOCK_ACCESS_FS_MAKE_CHAR.toULong()),
    MakeDir(LANDLOCK_ACCESS_FS_MAKE_DIR.toULong()),
    MakeReg(LANDLOCK_ACCESS_FS_MAKE_REG.toULong()),
    MakeSock(LANDLOCK_ACCESS_FS_MAKE_SOCK.toULong()),
    MakeFifo(LANDLOCK_ACCESS_FS_MAKE_FIFO.toULong()),
    MakeBlock(LANDLOCK_ACCESS_FS_MAKE_BLOCK.toULong()),
    MakeSym(LANDLOCK_ACCESS_FS_MAKE_SYM.toULong()),
    Refer(LANDLOCK_ACCESS_FS_REFER.toULong()),
    Truncate(LANDLOCK_ACCESS_FS_TRUNCATE.toULong()),
    IoctlDev(LANDLOCK_ACCESS_FS_IOCTL_DEV.toULong()),
    ;

    companion object {
        val ALL: BitFlags = BitFlags.from(entries)

        val ACCESS_FILE: BitFlags = BitFlags.from(ReadFile, WriteFile, Execute, Truncate, IoctlDev)

        fun fromAll(abi: ABI): BitFlags = fromRead(abi) or fromWrite(abi)

        fun fromRead(abi: ABI): BitFlags =
            when (abi) {
                ABI.Unsupported -> BitFlags.empty()
                ABI.V1, ABI.V2, ABI.V3, ABI.V4, ABI.V5, ABI.V6 -> BitFlags.from(Execute, ReadFile, ReadDir)
            }

        fun fromWrite(abi: ABI): BitFlags =
            when (abi) {
                ABI.Unsupported -> BitFlags.empty()
                ABI.V1 ->
                    BitFlags.from(
                        WriteFile,
                        RemoveDir,
                        RemoveFile,
                        MakeChar,
                        MakeDir,
                        MakeReg,
                        MakeSock,
                        MakeFifo,
                        MakeBlock,
                        MakeSym,
                    )
                ABI.V2 -> fromWrite(ABI.V1) or Refer
                ABI.V3, ABI.V4 -> fromWrite(ABI.V2) or Truncate
                ABI.V5, ABI.V6 -> fromWrite(ABI.V4) or IoctlDev
            }

        fun fromFile(abi: ABI): BitFlags = fromAll(abi) and ACCESS_FILE
    }
}

/**
 * File descriptor representation for a path beneath rule.
 */
data class PathFd(
    val path: String,
    val fd: Int = 0,
) {
    companion object {
        fun open(path: String): Result<PathFd> = Result.success(PathFd(path, 0))
    }
}

/**
 * Landlock rule for a file hierarchy beneath a parent path.
 */
data class PathBeneath(
    val parent: PathFd,
    var allowedAccess: BitFlags,
    var compatLevel: CompatLevel? = null,
) : Rule<AccessFs>,
    Compatible {
    constructor(path: String, access: BitFlags) : this(PathFd(path), access)
    constructor(path: String, access: AccessFs) : this(PathFd(path), BitFlags.from(access))
    constructor(parent: PathFd, access: AccessFs) : this(parent, BitFlags.from(access))

    override fun setCompatibility(level: CompatLevel): PathBeneath {
        this.compatLevel = level
        return this
    }

    override fun checkConsistency(ruleset: RulesetCreated): Result<Unit> {
        if (ruleset.requestedHandledFs.contains(allowedAccess)) {
            return Result.success(Unit)
        }
        val incompatible = allowedAccess and !ruleset.requestedHandledFs
        return Result.failure(
            RulesetError.AddRules(
                AddRulesError.Fs(
                    AddRuleError.UnhandledAccess(
                        access = allowedAccess,
                        incompatible = incompatible,
                    ),
                ),
            ),
        )
    }

    fun toAttr(): LandlockPathBeneathAttr =
        LandlockPathBeneathAttr(
            allowedAccess = allowedAccess.bits,
            parentFd = parent.fd,
        )
}

/**
 * Helper to construct path beneath rules from an iterable of path strings.
 */
fun pathBeneathRules(
    paths: Iterable<String>,
    access: BitFlags,
): List<Result<PathBeneath>> =
    paths.map { path ->
        PathFd.open(path).map { fd -> PathBeneath(fd, access) }
    }
