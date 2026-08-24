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
        val errno: Int,
        val detailMessage: String = "failed to create a ruleset: errno $errno",
    ) : CreateRulesetError(detailMessage)

    data object MissingHandledAccess : CreateRulesetError("missing access")
}

sealed class AddRuleError(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause) {
    data class AddRuleCall(
        val errno: Int,
        val detailMessage: String = "failed to add a rule: errno $errno",
    ) : AddRuleError(detailMessage)

    data class UnhandledAccess(
        val access: BitFlags<*>,
        val incompatible: BitFlags<*>,
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
        val errno: Int,
        val detailMessage: String = "failed to check file descriptor type: errno $errno",
    ) : PathBeneathError(detailMessage)

    data class DirectoryAccess(
        val access: BitFlags<AccessFs>,
        val incompatible: BitFlags<AccessFs>,
    ) : PathBeneathError("incompatible directory-only access-rights: $incompatible")
}

sealed class AccessError(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause) {
    data object Empty : AccessError("empty access-right")

    data class Unknown(
        val access: BitFlags<*>,
        val unknown: BitFlags<*>,
    ) : AccessError("unknown access-rights (at build time): $unknown")

    data class Incompatible(
        val access: BitFlags<*>,
    ) : AccessError("fully incompatible access-rights: $access")

    data class PartiallyCompatible(
        val access: BitFlags<*>,
        val incompatible: BitFlags<*>,
    ) : AccessError("partially incompatible access-rights: $incompatible")
}

sealed class RestrictSelfError(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause) {
    data class SetNoNewPrivsCall(
        val errno: Int,
        val detailMessage: String = "failed to set no_new_privs: errno $errno",
    ) : RestrictSelfError(detailMessage)

    data class RestrictSelfCall(
        val errno: Int,
        val detailMessage: String = "failed to restrict the calling thread: errno $errno",
    ) : RestrictSelfError(detailMessage)
}

sealed class PathFdError(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause) {
    data class OpenCall(
        val path: String,
        val errno: Int,
        val detailMessage: String = "failed to open \"$path\": errno $errno",
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
                is CreateRulesetError.CreateRulesetCall -> Errno(error.errno)
                is AddRuleError.AddRuleCall -> Errno(error.errno)
                is RestrictSelfError.SetNoNewPrivsCall -> Errno(error.errno)
                is RestrictSelfError.RestrictSelfCall -> Errno(error.errno)
                is PathFdError.OpenCall -> Errno(error.errno)
                is PathBeneathError.StatCall -> Errno(error.errno)
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
