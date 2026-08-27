// port-lint: source errors.rs
package io.github.kotlinmania.landlock

sealed class RulesetError(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause) {
    data class HandleAccesses(
        val error: HandleAccessesError,
    ) : RulesetError(error.message, error)

    data class CreateRuleset(
        val error: CreateRulesetError,
    ) : RulesetError(error.message, error)

    data class AddRules(
        val error: AddRulesError,
    ) : RulesetError(error.message, error)

    data class RestrictSelf(
        val error: RestrictSelfError,
    ) : RulesetError(error.message, error)

    data class Scope(
        val error: ScopeError,
    ) : RulesetError(error.message, error)
}

sealed class HandleAccessError(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause) {
    data class Compat(
        val error: CompatError,
    ) : HandleAccessError(error.message, error)
}

sealed class ScopeError(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause) {
    data class Compat(
        val error: CompatError,
    ) : ScopeError(error.message, error)
}

sealed class HandleAccessesError(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause) {
    data class Fs(
        val error: HandleAccessError,
    ) : HandleAccessesError(error.message, error)

    data class Net(
        val error: HandleAccessError,
    ) : HandleAccessesError(error.message, error)
}

sealed class CreateRulesetError(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause) {
    data class CreateRulesetCall(
        val rawErrno: Int,
        val detailMessage: String = "failed to create a ruleset: errno $rawErrno",
    ) : CreateRulesetError(detailMessage)

    data object MissingHandledAccess : CreateRulesetError("missing access")
}

sealed class AddRuleError(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause) {
    data class AddRuleCall(
        val rawErrno: Int,
        val detailMessage: String = "failed to add a rule: errno $rawErrno",
    ) : AddRuleError(detailMessage)

    data class UnhandledAccess(
        val access: BitFlags,
        val incompatible: BitFlags,
    ) : AddRuleError("access-rights not handled by the ruleset: $incompatible")

    data class Compat(
        val error: CompatError,
    ) : AddRuleError(error.message, error)
}

sealed class AddRulesError(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause) {
    data class Fs(
        val error: AddRuleError,
    ) : AddRulesError(error.message, error)

    data class Net(
        val error: AddRuleError,
    ) : AddRulesError(error.message, error)
}

sealed class CompatError(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause) {
    data class PathBeneath(
        val error: PathBeneathError,
    ) : CompatError(error.message, error)

    data class Access(
        val error: AccessError,
    ) : CompatError(error.message, error)
}

sealed class PathBeneathError(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause) {
    data class StatCall(
        val rawErrno: Int,
        val detailMessage: String = "failed to check file descriptor type: errno $rawErrno",
    ) : PathBeneathError(detailMessage)

    data class DirectoryAccess(
        val access: BitFlags,
        val incompatible: BitFlags,
    ) : PathBeneathError("incompatible directory-only access-rights: $incompatible")
}

sealed class AccessError(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause) {
    data object Empty : AccessError("empty access-right")

    data class Unknown(
        val access: BitFlags,
        val unknown: BitFlags,
    ) : AccessError("unknown access-rights (at build time): $unknown")

    data class Incompatible(
        val access: BitFlags,
    ) : AccessError("fully incompatible access-rights: $access")

    data class PartiallyCompatible(
        val access: BitFlags,
        val incompatible: BitFlags,
    ) : AccessError("partially incompatible access-rights: $incompatible")
}

sealed class RestrictSelfError(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause) {
    data class SetNoNewPrivsCall(
        val rawErrno: Int,
        val detailMessage: String = "failed to set no_new_privs: errno $rawErrno",
    ) : RestrictSelfError(detailMessage)

    data class RestrictSelfCall(
        val rawErrno: Int,
        val detailMessage: String = "failed to restrict the calling thread: errno $rawErrno",
    ) : RestrictSelfError(detailMessage)
}

sealed class PathFdError(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause) {
    data class OpenCall(
        val path: String,
        val rawErrno: Int,
        val detailMessage: String = "failed to open \"$path\": errno $rawErrno",
    ) : PathFdError(detailMessage)
}

data class Errno(
    val value: Int,
) {
    companion object {
        const val EINVAL = 22
        const val EACCES = 13
        const val EIO = 5
        const val ENOSYS = 38
        const val EOPNOTSUPP = 95
        const val E2BIG = 7

        fun from(error: Throwable): Errno =
            when (error) {
                is CreateRulesetError.CreateRulesetCall -> Errno(error.rawErrno)
                is AddRuleError.AddRuleCall -> Errno(error.rawErrno)
                is RestrictSelfError.SetNoNewPrivsCall -> Errno(error.rawErrno)
                is RestrictSelfError.RestrictSelfCall -> Errno(error.rawErrno)
                is PathFdError.OpenCall -> Errno(error.rawErrno)
                is PathBeneathError.StatCall -> Errno(error.rawErrno)
                is RulesetError.CreateRuleset -> from(error.error)
                is RulesetError.AddRules -> from(error.error)
                is RulesetError.RestrictSelf -> from(error.error)
                is RulesetError.HandleAccesses -> from(error.error)
                is RulesetError.Scope -> from(error.error)
                is AddRulesError.Fs -> from(error.error)
                is AddRulesError.Net -> from(error.error)
                else -> Errno(EINVAL)
            }
    }
}
