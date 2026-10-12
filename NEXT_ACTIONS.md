# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 12/12 (100.0%)
- **Function parity:** 54/118 matched (target 132) — 45.8%
- **Class/type parity:** 50/55 matched (target 88) — 90.9%
- **Combined symbol parity:** 104/173 matched (target 220) — 60.1%
- **Average inline-code cosine:** 0.34 (function body across 10 matched files)
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
- **Similarity:** 0.22
- **Dependents:** 0
- **Priority Score:** 233807.8
- **Functions:** 10/30 matched (target 18)
- **Missing functions:** `prctl_set_no_new_privs`, `support_no_new_privs`, `ruleset_add_rule_iter`, `default`, `new`, `as_mut`, `ruleset_as_mut`, `ruleset_attr`, `ruleset_created_handle_access_fs`, `ruleset_created_handle_access_net_tcp`, `ruleset_created_scope`, `ruleset_created_fs_net_scope`, `ruleset_created_ownedfd_none`, `ruleset_created_attr`, `ruleset_compat_dummy`, `ruleset_compat_partial`, `ruleset_unsupported`, `ignore_abi_v2_with_abi_v1`, `unsupported_handled_access`, `unsupported_handled_access_errno`
- **Types:** 5/8 matched (target 5)
- **Missing types:** `PrivateRule`, `RulesetAttr`, `RulesetCreatedAttr`
- **Tests:** 0/15 matched

### 3. fs

- **Target:** `landlock.Fs`
- **Similarity:** 0.32
- **Dependents:** 0
- **Priority Score:** 132606.8
- **Functions:** 10/23 matched (target 14)
- **Missing functions:** `consistent_access_fs_rw`, `is_file`, `new`, `try_compat_children`, `try_compat_inner`, `path_beneath_try_compat_children`, `path_beneath_try_compat`, `path_beneath_compatibility`, `as_ptr`, `path_beneath_check_consistency`, `as_fd`, `path_fd`, `path_beneath_rules_iter`
- **Types:** 3/3 matched
- **Missing types:** _none_
- **Tests:** 0/7 matched

### 4. compat

- **Target:** `landlock.Compat [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 123310.0
- **Functions:** 11/23 matched (target 30)
- **Missing functions:** `is_known`, `abi_from`, `known_abi`, `fmt`, `can_emulate`, `get_errno_from_landlock_status`, `current_kernel_abi`, `compat_state_update_1`, `compat_state_update_2`, `status`, `deprecated_set_best_effort`, `try_compat`
- **Types:** 10/10 matched (target 17)
- **Missing types:** _none_
- **Tests:** 1/10 matched

### 5. lib

- **Target:** `landlock.Lib`
- **Similarity:** 0.52
- **Dependents:** 0
- **Priority Score:** 51604.8
- **Functions:** 11/15 matched (target 24)
- **Missing functions:** `check_ruleset_support`, `too_much_access_rights_for_a_file`, `path_beneath_rules_with_too_much_access_rights_for_a_file`, `ruleset_created_try_clone_ownedfd`
- **Types:** 0/1 matched (target 2)
- **Missing types:** `Sealed`
- **Tests:** 11/15 matched

### 6. net

- **Target:** `landlock.Net`
- **Similarity:** 0.39
- **Dependents:** 0
- **Priority Score:** 51306.1
- **Functions:** 6/11 matched (target 9)
- **Missing functions:** `new`, `as_ptr`, `net_port_check_consistency`, `try_compat_children`, `try_compat_inner`
- **Types:** 2/2 matched
- **Missing types:** _none_
- **Tests:** 0/1 matched

### 7. errors

- **Target:** `landlock.Errors`
- **Similarity:** 0.20
- **Dependents:** 0
- **Priority Score:** 32008.0
- **Functions:** 3/5 matched (target 8)
- **Missing functions:** `ruleset_error_breaking_change`, `deref`
- **Types:** 14/15 matched (target 41)
- **Missing types:** `Target`
- **Tests:** 1/2 matched

### 8. uapi.landlock_i686

- **Target:** `i686.LandlockI686`
- **Similarity:** 0.22
- **Dependents:** 0
- **Priority Score:** 30907.8
- **Functions:** 0/3 matched
- **Missing functions:** `bindgen_test_layout_landlock_ruleset_attr`, `bindgen_test_layout_landlock_path_beneath_attr`, `bindgen_test_layout_landlock_net_port_attr`
- **Types:** 6/6 matched
- **Missing types:** _none_
- **Tests:** 0/3 matched

### 9. uapi.landlock_x86_64

- **Target:** `x8664.LandlockX8664`
- **Similarity:** 0.22
- **Dependents:** 0
- **Priority Score:** 30907.8
- **Functions:** 0/3 matched
- **Missing functions:** `bindgen_test_layout_landlock_ruleset_attr`, `bindgen_test_layout_landlock_path_beneath_attr`, `bindgen_test_layout_landlock_net_port_attr`
- **Types:** 6/6 matched
- **Missing types:** _none_
- **Tests:** 0/3 matched

### 10. access

- **Target:** `landlock.Access`
- **Similarity:** 0.39
- **Dependents:** 0
- **Priority Score:** 20706.1
- **Functions:** 2/4 matched (target 22)
- **Missing functions:** `bit_flags_full_negation`, `compat_bit_flags`
- **Types:** 3/3 matched (target 5)
- **Missing types:** _none_
- **Tests:** 0/2 matched

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

