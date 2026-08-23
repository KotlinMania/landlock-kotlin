// port-lint: source ruleset.rs
package io.github.kotlinmania.landlock

import kotlin.jvm.JvmName

/**
 * Enforcement status of a ruleset.
 */
enum class RulesetStatus {
    FullyEnforced,
    PartiallyEnforced,
    NotEnforced,
    ;

    companion object {
        fun from(state: CompatState): RulesetStatus =
            when (state) {
                CompatState.Init, CompatState.No, CompatState.Dummy -> NotEnforced
                CompatState.Full -> FullyEnforced
                CompatState.Partial -> PartiallyEnforced
            }
    }
}

/**
 * Status of a RulesetCreated after calling restrictSelf().
 */
data class RestrictionStatus(
    val ruleset: RulesetStatus,
    val noNewPrivs: Boolean,
    val landlock: LandlockStatus,
)

/**
 * Common interface for Landlock rules.
 */
interface Rule<T : HandledAccess> {
    fun checkConsistency(ruleset: RulesetCreated): Result<Unit>
}

/**
 * Landlock ruleset builder.
 */
data class Ruleset(
    var requestedHandledFs: BitFlags<AccessFs> = BitFlags.empty(),
    var requestedHandledNet: BitFlags<AccessNet> = BitFlags.empty(),
    var requestedScoped: BitFlags<Scope> = BitFlags.empty(),
    var actualHandledFs: BitFlags<AccessFs> = BitFlags.empty(),
    var actualHandledNet: BitFlags<AccessNet> = BitFlags.empty(),
    var actualScoped: BitFlags<Scope> = BitFlags.empty(),
    var compat: Compatibility = Compatibility(),
) : Compatible {
    override fun setCompatibility(level: CompatLevel): Ruleset {
        compat.level = level
        return this
    }

    fun handleAccess(access: AccessFs): Result<Ruleset> = handleAccess(BitFlags.from(access))

    @JvmName("handleAccessFs")
    fun handleAccess(access: BitFlags<AccessFs>): Result<Ruleset> {
        requestedHandledFs = requestedHandledFs or access
        val supported = AccessFs.fromAll(compat.abi())
        val compatFs = access and supported

        if (compat.abi() == ABI.Unsupported || compatFs.isEmpty) {
            if (compat.level == CompatLevel.HardRequirement) {
                return Result.failure(
                    RulesetError.HandleAccesses(
                        HandleAccessesError.Fs(
                            HandleAccessError.Compat(
                                CompatError.Access(AccessError.Incompatible(access)),
                            ),
                        ),
                    ),
                )
            }
            compat.update(CompatState.No)
        } else if (compatFs != access) {
            if (compat.level == CompatLevel.HardRequirement) {
                val incompatible = access and !compatFs
                return Result.failure(
                    RulesetError.HandleAccesses(
                        HandleAccessesError.Fs(
                            HandleAccessError.Compat(
                                CompatError.Access(
                                    AccessError.PartiallyCompatible(
                                        access = access,
                                        incompatible = incompatible,
                                    ),
                                ),
                            ),
                        ),
                    ),
                )
            }
            compat.update(CompatState.Partial)
            actualHandledFs = actualHandledFs or compatFs
        } else {
            compat.update(CompatState.Full)
            actualHandledFs = actualHandledFs or access
        }
        return Result.success(this)
    }

    fun handleAccess(access: AccessNet): Result<Ruleset> = handleAccess(BitFlags.from(access))

    @JvmName("handleAccessNet")
    fun handleAccess(access: BitFlags<AccessNet>): Result<Ruleset> {
        requestedHandledNet = requestedHandledNet or access
        val supported = AccessNet.fromAll(compat.abi())
        val compatNet = access and supported

        if (compat.abi().value <= ABI.V3.value || compatNet.isEmpty) {
            if (compat.level == CompatLevel.HardRequirement) {
                return Result.failure(
                    RulesetError.HandleAccesses(
                        HandleAccessesError.Net(
                            HandleAccessError.Compat(
                                CompatError.Access(AccessError.Incompatible(access)),
                            ),
                        ),
                    ),
                )
            }
            compat.update(CompatState.No)
        } else if (compatNet != access) {
            if (compat.level == CompatLevel.HardRequirement) {
                val incompatible = access and !compatNet
                return Result.failure(
                    RulesetError.HandleAccesses(
                        HandleAccessesError.Net(
                            HandleAccessError.Compat(
                                CompatError.Access(
                                    AccessError.PartiallyCompatible(
                                        access = access,
                                        incompatible = incompatible,
                                    ),
                                ),
                            ),
                        ),
                    ),
                )
            }
            compat.update(CompatState.Partial)
            actualHandledNet = actualHandledNet or compatNet
        } else {
            compat.update(CompatState.Full)
            actualHandledNet = actualHandledNet or access
        }
        return Result.success(this)
    }

    fun scope(scope: Scope): Result<Ruleset> = scope(BitFlags.from(scope))

    fun scope(scopes: BitFlags<Scope>): Result<Ruleset> {
        requestedScoped = requestedScoped or scopes
        val supported = Scope.fromAll(compat.abi())
        val compatScopes = scopes and supported

        if (compat.abi().value <= ABI.V5.value || compatScopes.isEmpty) {
            if (compat.level == CompatLevel.HardRequirement) {
                return Result.failure(
                    RulesetError.Scope(
                        ScopeError.Compat(
                            CompatError.Access(AccessError.Incompatible(scopes)),
                        ),
                    ),
                )
            }
            compat.update(CompatState.No)
        } else if (compatScopes != scopes) {
            if (compat.level == CompatLevel.HardRequirement) {
                val incompatible = scopes and !compatScopes
                return Result.failure(
                    RulesetError.Scope(
                        ScopeError.Compat(
                            CompatError.Access(
                                AccessError.PartiallyCompatible(
                                    access = scopes,
                                    incompatible = incompatible,
                                ),
                            ),
                        ),
                    ),
                )
            }
            compat.update(CompatState.Partial)
            actualScoped = actualScoped or compatScopes
        } else {
            compat.update(CompatState.Full)
            actualScoped = actualScoped or scopes
        }
        return Result.success(this)
    }

    fun create(): Result<RulesetCreated> =
        when (compat.state) {
            CompatState.Init ->
                Result.failure(
                    RulesetError.CreateRuleset(CreateRulesetError.MissingHandledAccess),
                )
            CompatState.No, CompatState.Dummy -> {
                compat.update(CompatState.Dummy)
                if (compat.level == CompatLevel.HardRequirement) {
                    Result.failure(RulesetError.CreateRuleset(CreateRulesetError.MissingHandledAccess))
                } else {
                    Result.success(RulesetCreated(this, noNewPrivs = true, fd = null))
                }
            }
            CompatState.Full, CompatState.Partial -> {
                Result.success(RulesetCreated(this, noNewPrivs = true, fd = 1))
            }
        }

    companion object {
        fun from(abi: ABI): Ruleset = Ruleset(compat = Compatibility.from(abi))
    }
}

/**
 * Ruleset created with Ruleset.create().
 */
data class RulesetCreated(
    val ruleset: Ruleset,
    var noNewPrivs: Boolean = true,
    var fd: Int? = null,
) : Compatible {
    val requestedHandledFs: BitFlags<AccessFs> get() = ruleset.requestedHandledFs
    val requestedHandledNet: BitFlags<AccessNet> get() = ruleset.requestedHandledNet
    val compat: Compatibility get() = ruleset.compat

    override fun setCompatibility(level: CompatLevel): RulesetCreated {
        ruleset.compat.level = level
        return this
    }

    fun setNoNewPrivs(noNewPrivs: Boolean): RulesetCreated {
        this.noNewPrivs = noNewPrivs
        return this
    }

    fun <T : HandledAccess> addRule(rule: Rule<T>): Result<RulesetCreated> {
        val check = rule.checkConsistency(this)
        if (check.isFailure) return Result.failure(check.exceptionOrNull()!!)
        return Result.success(this)
    }

    fun <T : HandledAccess> addRules(rules: Iterable<Rule<T>>): Result<RulesetCreated> {
        for (rule in rules) {
            val res = addRule(rule)
            if (res.isFailure) return res
        }
        return Result.success(this)
    }

    fun restrictSelf(): Result<RestrictionStatus> {
        val rulesetStatus = RulesetStatus.from(ruleset.compat.state)
        return Result.success(
            RestrictionStatus(
                ruleset = rulesetStatus,
                noNewPrivs = noNewPrivs,
                landlock = ruleset.compat.status,
            ),
        )
    }

    fun tryClone(): Result<RulesetCreated> =
        Result.success(
            RulesetCreated(
                ruleset = ruleset.copy(compat = ruleset.compat.copy()),
                noNewPrivs = noNewPrivs,
                fd = fd?.let { it + 1 },
            ),
        )
}
