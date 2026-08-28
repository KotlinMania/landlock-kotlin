# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 12/12 (100.0%)
- **Function parity:** 22/118 matched (target 86) — 18.6%
- **Class/type parity:** 45/55 matched (target 79) — 81.8%
- **Combined symbol parity:** 67/173 matched (target 165) — 38.7%
- **Average inline-code cosine:** 0.19 (function body across 10 matched files)
- **Average documentation cosine:** 0.21 (doc text across 10 matched files)
- **Cheat-zeroed Files:** 1
- **Critical Issues:** 11 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. scope

- **Target:** `landlock.Scope`
- **Similarity:** 0.93
- **Dependents:** 1
- **Priority Score:** 1000200.7
- **Functions:** 1/1 matched
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_

### 2. ruleset

- **Target:** `landlock.Ruleset`
- **Similarity:** 0.17
- **Dependents:** 0
- **Priority Score:** 243808.3
- **Functions:** 9/30 matched (target 16)
- **Missing functions:** `prctl_set_no_new_privs`, `support_no_new_privs`, `ruleset_add_rule_iter`, `default`, `new`, `as_option_compat_level_mut`, `as_mut`, `ruleset_as_mut`, `ruleset_attr`, `ruleset_created_handle_access_fs`, `ruleset_created_handle_access_net_tcp`, `ruleset_created_scope`, `ruleset_created_fs_net_scope`, `ruleset_created_ownedfd_none`, `ruleset_created_attr`, `ruleset_compat_dummy`, `ruleset_compat_partial`, `ruleset_unsupported`, `ignore_abi_v2_with_abi_v1`, `unsupported_handled_access`, `unsupported_handled_access_errno`
- **Types:** 5/8 matched (target 5)
- **Missing types:** `PrivateRule`, `RulesetAttr`, `RulesetCreatedAttr`
- **Tests:** 0/15 matched

### 3. compat

- **Target:** `landlock.Compat [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 233310.0
- **Functions:** 3/23 matched (target 15)
- **Missing functions:** `is_known`, `abi_from`, `known_abi`, `fmt`, `current`, `test_current_landlock_status`, `can_emulate`, `get_errno_from_landlock_status`, `current_kernel_abi`, `compat_state_update_1`, `compat_state_update_2`, `new`, `status`, `set_compatibility`, `set_best_effort`, `deprecated_set_best_effort`, `tailored_compat_level`, `new_path`, `try_compat_children`, `try_compat`
- **Types:** 7/10 matched (target 14)
- **Missing types:** `OptionCompatLevelMut`, `TailoredCompatLevel`, `TryCompat`
- **Tests:** 0/10 matched

### 4. fs

- **Target:** `landlock.Fs`
- **Similarity:** 0.19
- **Dependents:** 0
- **Priority Score:** 172608.1
- **Functions:** 6/23 matched (target 9)
- **Missing functions:** `consistent_access_fs_rw`, `ruleset_handle_access`, `into_add_rules_error`, `into_handle_accesses_error`, `is_file`, `new`, `try_compat_children`, `try_compat_inner`, `path_beneath_try_compat_children`, `path_beneath_try_compat`, `as_option_compat_level_mut`, `path_beneath_compatibility`, `as_ptr`, `path_beneath_check_consistency`, `as_fd`, `path_fd`, `path_beneath_rules_iter`
- **Types:** 3/3 matched
- **Missing types:** _none_
- **Tests:** 0/7 matched

### 5. lib

- **Target:** `landlock.Lib`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 161610.0
- **Functions:** 0/15 matched (target 18)
- **Missing functions:** `check_ruleset_support`, `allow_root_compat`, `too_much_access_rights_for_a_file`, `path_beneath_rules_with_too_much_access_rights_for_a_file`, `allow_root_fragile`, `ruleset_enforced`, `abi_v2_exec_refer`, `abi_v2_refer_only`, `abi_v3_truncate`, `ruleset_created_try_clone`, `abi_v4_tcp`, `abi_v5_ioctl_dev`, `abi_v6_scope_mix`, `abi_v6_scope_only`, `ruleset_created_try_clone_ownedfd`
- **Types:** 0/1 matched (target 2)
- **Missing types:** `Sealed`
- **Tests:** 0/15 matched

### 6. net

- **Target:** `landlock.Net`
- **Similarity:** 0.15
- **Dependents:** 0
- **Priority Score:** 91308.5
- **Functions:** 2/11 matched (target 4)
- **Missing functions:** `ruleset_handle_access`, `into_add_rules_error`, `into_handle_accesses_error`, `new`, `as_ptr`, `net_port_check_consistency`, `try_compat_children`, `try_compat_inner`, `as_option_compat_level_mut`
- **Types:** 2/2 matched
- **Missing types:** _none_
- **Tests:** 0/1 matched

### 7. errors

- **Target:** `landlock.Errors`
- **Similarity:** 0.04
- **Dependents:** 0
- **Priority Score:** 62009.6
- **Functions:** 1/5 matched (target 3)
- **Missing functions:** `ruleset_error_breaking_change`, `new`, `deref`, `_test_ruleset_errno`
- **Types:** 13/15 matched (target 37)
- **Missing types:** `TestRulesetError`, `Target`
- **Tests:** 0/2 matched

### 8. access

- **Target:** `landlock.Access`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 50710.0
- **Functions:** 0/4 matched (target 14)
- **Missing functions:** `full_negation`, `bit_flags_full_negation`, `try_compat_inner`, `compat_bit_flags`
- **Types:** 2/3 matched
- **Missing types:** `PrivateHandledAccess`
- **Tests:** 0/2 matched

### 9. uapi.landlock_i686

- **Target:** `i686.LandlockI686`
- **Similarity:** 0.22
- **Dependents:** 0
- **Priority Score:** 30907.8
- **Functions:** 0/3 matched
- **Missing functions:** `bindgen_test_layout_landlock_ruleset_attr`, `bindgen_test_layout_landlock_path_beneath_attr`, `bindgen_test_layout_landlock_net_port_attr`
- **Types:** 6/6 matched
- **Missing types:** _none_
- **Tests:** 0/3 matched

### 10. uapi.landlock_x86_64

- **Target:** `x8664.LandlockX8664`
- **Similarity:** 0.22
- **Dependents:** 0
- **Priority Score:** 30907.8
- **Functions:** 0/3 matched
- **Missing functions:** `bindgen_test_layout_landlock_ruleset_attr`, `bindgen_test_layout_landlock_path_beneath_attr`, `bindgen_test_layout_landlock_net_port_attr`
- **Types:** 6/6 matched
- **Missing types:** _none_
- **Tests:** 0/3 matched

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

## Reexport / Wiring Modules

These files match `reexport_modules` patterns in `.ast_distance_config.json`. They are filtered out of
normal priority and missing-file ladders because they are wiring
modules, not direct logic ports. Consult them for call-site routing;
do not treat them as the next implementation target by default.

### Matched

| Source | Target | Path |
|--------|--------|------|
| `uapi.mod` | `uapi.Mod` | `uapi/mod` |
| `uapi.landlock_all` | `uapi.LandlockAll` | `uapi/landlock_all` |

