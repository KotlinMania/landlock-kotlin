# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 12/13 (92.3%)
- **Function parity:** 22/126 matched (target 89) — 17.5%
- **Class/type parity:** 51/63 matched (target 87) — 81.0%
- **Combined symbol parity:** 73/189 matched (target 176) — 38.6%
- **Average inline-code cosine:** 0.19 (function body across 10 matched files)
- **Average documentation cosine:** 0.21 (doc text across 10 matched files)
- **Cheat-zeroed Files:** 3
- **Critical Issues:** 11 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. scope

- **Target:** `landlock.Scope [PROVENANCE-FALLBACK]`
- **Similarity:** 0.93
- **Dependents:** 1
- **Priority Score:** 1000200.7
- **Functions:** 1/1 matched
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `scope.rs` vs expected `scope.rs`
- **Proposed provenance header:** `// port-lint: source scope.rs` (current: `// port-lint: source scope.rs`)
- **Lint issues:** 1

### 2. ruleset

- **Target:** `landlock.Ruleset [PROVENANCE-FALLBACK]`
- **Similarity:** 0.17
- **Dependents:** 0
- **Priority Score:** 243808.3
- **Functions:** 9/30 matched (target 16)
- **Missing functions:** `prctl_set_no_new_privs`, `support_no_new_privs`, `ruleset_add_rule_iter`, `default`, `new`, `as_option_compat_level_mut`, `as_mut`, `ruleset_as_mut`, `ruleset_attr`, `ruleset_created_handle_access_fs`, `ruleset_created_handle_access_net_tcp`, `ruleset_created_scope`, `ruleset_created_fs_net_scope`, `ruleset_created_ownedfd_none`, `ruleset_created_attr`, `ruleset_compat_dummy`, `ruleset_compat_partial`, `ruleset_unsupported`, `ignore_abi_v2_with_abi_v1`, `unsupported_handled_access`, `unsupported_handled_access_errno`
- **Types:** 5/8 matched (target 5)
- **Missing types:** `PrivateRule`, `RulesetAttr`, `RulesetCreatedAttr`
- **Tests:** 0/15 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `ruleset.rs` vs expected `ruleset.rs`
- **Proposed provenance header:** `// port-lint: source ruleset.rs` (current: `// port-lint: source ruleset.rs`)
- **Lint issues:** 1

### 3. compat

- **Target:** `landlock.Compat [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 233310.0
- **Functions:** 3/23 matched (target 15)
- **Missing functions:** `is_known`, `abi_from`, `known_abi`, `fmt`, `current`, `test_current_landlock_status`, `can_emulate`, `get_errno_from_landlock_status`, `current_kernel_abi`, `compat_state_update_1`, `compat_state_update_2`, `new`, `status`, `set_compatibility`, `set_best_effort`, `deprecated_set_best_effort`, `tailored_compat_level`, `new_path`, `try_compat_children`, `try_compat`
- **Types:** 7/10 matched (target 14)
- **Missing types:** `OptionCompatLevelMut`, `TailoredCompatLevel`, `TryCompat`
- **Tests:** 0/10 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `compat.rs` vs expected `compat.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:compat.rs` vs expected `compat.rs`
- **Proposed provenance header:** `// port-lint: source compat.rs` (current: `// port-lint: source compat.rs`)
- **Proposed provenance header:** `// port-lint: tests compat.rs` (current: `// port-lint: tests compat.rs`)
- **Lint issues:** 2

### 4. fs

- **Target:** `landlock.Fs [PROVENANCE-FALLBACK]`
- **Similarity:** 0.19
- **Dependents:** 0
- **Priority Score:** 172608.1
- **Functions:** 6/23 matched (target 9)
- **Missing functions:** `consistent_access_fs_rw`, `ruleset_handle_access`, `into_add_rules_error`, `into_handle_accesses_error`, `is_file`, `new`, `try_compat_children`, `try_compat_inner`, `path_beneath_try_compat_children`, `path_beneath_try_compat`, `as_option_compat_level_mut`, `path_beneath_compatibility`, `as_ptr`, `path_beneath_check_consistency`, `as_fd`, `path_fd`, `path_beneath_rules_iter`
- **Types:** 3/3 matched
- **Missing types:** _none_
- **Tests:** 0/7 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `fs.rs` vs expected `fs.rs`
- **Proposed provenance header:** `// port-lint: source fs.rs` (current: `// port-lint: source fs.rs`)
- **Lint issues:** 1

### 5. lib

- **Target:** `landlock.Lib [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 161610.0
- **Functions:** 0/15 matched (target 18)
- **Missing functions:** `check_ruleset_support`, `allow_root_compat`, `too_much_access_rights_for_a_file`, `path_beneath_rules_with_too_much_access_rights_for_a_file`, `allow_root_fragile`, `ruleset_enforced`, `abi_v2_exec_refer`, `abi_v2_refer_only`, `abi_v3_truncate`, `ruleset_created_try_clone`, `abi_v4_tcp`, `abi_v5_ioctl_dev`, `abi_v6_scope_mix`, `abi_v6_scope_only`, `ruleset_created_try_clone_ownedfd`
- **Types:** 0/1 matched (target 2)
- **Missing types:** `Sealed`
- **Tests:** 0/15 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `lib.rs` vs expected `lib.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:lib.rs` vs expected `lib.rs`
- **Proposed provenance header:** `// port-lint: source lib.rs` (current: `// port-lint: source lib.rs`)
- **Proposed provenance header:** `// port-lint: tests lib.rs` (current: `// port-lint: tests lib.rs`)
- **Lint issues:** 2

### 6. net

- **Target:** `landlock.Net [PROVENANCE-FALLBACK]`
- **Similarity:** 0.15
- **Dependents:** 0
- **Priority Score:** 91308.5
- **Functions:** 2/11 matched (target 4)
- **Missing functions:** `ruleset_handle_access`, `into_add_rules_error`, `into_handle_accesses_error`, `new`, `as_ptr`, `net_port_check_consistency`, `try_compat_children`, `try_compat_inner`, `as_option_compat_level_mut`
- **Types:** 2/2 matched
- **Missing types:** _none_
- **Tests:** 0/1 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `net.rs` vs expected `net.rs`
- **Proposed provenance header:** `// port-lint: source net.rs` (current: `// port-lint: source net.rs`)
- **Lint issues:** 1

### 7. errors

- **Target:** `landlock.Errors [PROVENANCE-FALLBACK]`
- **Similarity:** 0.04
- **Dependents:** 0
- **Priority Score:** 62009.6
- **Functions:** 1/5 matched (target 3)
- **Missing functions:** `ruleset_error_breaking_change`, `new`, `deref`, `_test_ruleset_errno`
- **Types:** 13/15 matched (target 37)
- **Missing types:** `TestRulesetError`, `Target`
- **Tests:** 0/2 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `errors.rs` vs expected `errors.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:errors.rs` vs expected `errors.rs`
- **Proposed provenance header:** `// port-lint: source errors.rs` (current: `// port-lint: source errors.rs`)
- **Proposed provenance header:** `// port-lint: tests errors.rs` (current: `// port-lint: tests errors.rs`)
- **Lint issues:** 2

### 8. access

- **Target:** `landlock.Access [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 50710.0
- **Functions:** 0/4 matched (target 14)
- **Missing functions:** `full_negation`, `bit_flags_full_negation`, `try_compat_inner`, `compat_bit_flags`
- **Types:** 2/3 matched
- **Missing types:** `PrivateHandledAccess`
- **Tests:** 0/2 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `access.rs` vs expected `access.rs`
- **Proposed provenance header:** `// port-lint: source access.rs` (current: `// port-lint: source access.rs`)
- **Lint issues:** 1

### 9. uapi.landlock_i686

- **Target:** `i686.LandlockI686 [PROVENANCE-FALLBACK]`
- **Similarity:** 0.22
- **Dependents:** 0
- **Priority Score:** 30907.8
- **Functions:** 0/3 matched
- **Missing functions:** `bindgen_test_layout_landlock_ruleset_attr`, `bindgen_test_layout_landlock_path_beneath_attr`, `bindgen_test_layout_landlock_net_port_attr`
- **Types:** 6/6 matched
- **Missing types:** _none_
- **Tests:** 0/3 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `uapi/landlock_i686.rs` vs expected `uapi/landlock_i686.rs`
- **Proposed provenance header:** `// port-lint: source uapi/landlock_i686.rs` (current: `// port-lint: source uapi/landlock_i686.rs`)
- **Lint issues:** 1

### 10. uapi.landlock_x86_64

- **Target:** `x8664.LandlockX8664 [PROVENANCE-FALLBACK]`
- **Similarity:** 0.22
- **Dependents:** 0
- **Priority Score:** 30907.8
- **Functions:** 0/3 matched
- **Missing functions:** `bindgen_test_layout_landlock_ruleset_attr`, `bindgen_test_layout_landlock_path_beneath_attr`, `bindgen_test_layout_landlock_net_port_attr`
- **Types:** 6/6 matched
- **Missing types:** _none_
- **Tests:** 0/3 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `uapi/landlock_x86_64.rs` vs expected `uapi/landlock_x86_64.rs`
- **Proposed provenance header:** `// port-lint: source uapi/landlock_x86_64.rs` (current: `// port-lint: source uapi/landlock_x86_64.rs`)
- **Lint issues:** 1

### 11. uapi.mod

- **Target:** `uapi.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 30310.0
- **Functions:** 0/3 matched (target 0)
- **Missing functions:** `landlock_create_ruleset`, `landlock_add_rule`, `landlock_restrict_self`
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `uapi/mod.rs` vs expected `uapi/mod.rs`
- **Proposed provenance header:** `// port-lint: source uapi/mod.rs` (current: `// port-lint: source uapi/mod.rs`)
- **Lint issues:** 1

### 12. uapi.landlock_all

- **Target:** `uapi.LandlockAll [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 610.0
- **Functions:** 0/0 matched (target 3)
- **Missing functions:** _none_
- **Types:** 6/6 matched (target 7)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `uapi/landlock_all.rs` vs expected `uapi/landlock_all.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:uapi/landlock_all.rs` vs expected `uapi/landlock_all.rs`
- **Proposed provenance header:** `// port-lint: source uapi/landlock_all.rs` (current: `// port-lint: source uapi/landlock_all.rs`)
- **Proposed provenance header:** `// port-lint: tests uapi/landlock_all.rs` (current: `// port-lint: tests uapi/landlock_all.rs`)
- **Lint issues:** 2

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

