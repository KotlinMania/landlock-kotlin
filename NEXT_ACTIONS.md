# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 10/12 (83.3%)
- **Function parity:** 55/125 matched (target 118) — 44.0%
- **Class/type parity:** 54/54 matched (target 90) — 100.0%
- **Combined symbol parity:** 109/179 matched (target 208) — 60.9%
- **Average inline-code cosine:** 0.12 (function body across 9 matched files)
- **Average documentation cosine:** 0.20 (doc text across 9 matched files)
- **Cheat-zeroed Files:** 1
- **Critical Issues:** 10 files with <0.60 function similarity
- **Needs Review:** 0 files with 0.60-0.84 function similarity
- **Excellent:** 0 files with >=0.85 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. scope

- **Target:** `landlock.Scope`
- **Similarity:** 0.49
- **Dependents:** 1
- **Priority Score:** 1000205.1
- **Functions:** 1/1 matched
- **Missing functions:** _none_
- **Types:** 1/1 matched
- **Missing types:** _none_

### 2. ruleset

- **Target:** `landlock.Ruleset`
- **Similarity:** 0.07
- **Dependents:** 0
- **Priority Score:** 224609.3
- **Functions:** 16/38 matched (target 22)
- **Missing functions:** `prctl_set_no_new_privs`, `support_no_new_privs`, `from`, `ruleset_add_rule_iter`, `ruleset_as_mut`, `ruleset_attr`, `ruleset_created_handle_access_fs`, `ruleset_created_handle_access_net_tcp`, `ruleset_created_scope`, `ruleset_created_fs_net_scope`, `as_option_compat_level_mut`, `as_option_compat_level_mut`, `new`, `from`, `ruleset_created_ownedfd_none`, `ruleset_created_attr`, `ruleset_compat_dummy`, `ruleset_compat_partial`, `ruleset_unsupported`, `ignore_abi_v2_with_abi_v1`, `unsupported_handled_access`, `unsupported_handled_access_errno`
- **Types:** 8/8 matched
- **Missing types:** _none_
- **Tests:** 0/15 matched

### 3. compat

- **Target:** `landlock.Compat [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 174110.0
- **Functions:** 14/31 matched (target 32)
- **Missing functions:** `is_known`, `abi_from`, `known_abi`, `fmt`, `can_emulate`, `get_errno_from_landlock_status`, `current_kernel_abi`, `compat_state_update_1`, `compat_state_update_2`, `from`, `from`, `status`, `deprecated_set_best_effort`, `from`, `tailored_compat_level`, `new_path`, `try_compat`
- **Types:** 10/10 matched (target 17)
- **Missing types:** _none_
- **Tests:** 1/11 matched

### 4. fs

- **Target:** `landlock.Fs`
- **Similarity:** 0.16
- **Dependents:** 0
- **Priority Score:** 142808.4
- **Functions:** 11/25 matched (target 15)
- **Missing functions:** `consistent_access_fs_rw`, `is_file`, `new`, `try_compat_children`, `try_compat_inner`, `path_beneath_try_compat_children`, `path_beneath_try_compat`, `as_option_compat_level_mut`, `path_beneath_compatibility`, `path_beneath_check_consistency`, `new`, `as_fd`, `path_fd`, `path_beneath_rules_iter`
- **Types:** 3/3 matched
- **Missing types:** _none_
- **Tests:** 0/7 matched

### 5. net

- **Target:** `landlock.Net`
- **Similarity:** 0.20
- **Dependents:** 0
- **Priority Score:** 51408.0
- **Functions:** 7/12 matched (target 10)
- **Missing functions:** `new`, `net_port_check_consistency`, `try_compat_children`, `try_compat_inner`, `as_option_compat_level_mut`
- **Types:** 2/2 matched
- **Missing types:** _none_
- **Tests:** 0/1 matched

### 6. errors

- **Target:** `landlock.Errors`
- **Similarity:** 0.09
- **Dependents:** 0
- **Priority Score:** 42309.1
- **Functions:** 4/8 matched (target 10)
- **Missing functions:** `ruleset_error_breaking_change`, `from`, `from`, `test_ruleset_errno`
- **Types:** 15/15 matched (target 42)
- **Missing types:** _none_
- **Tests:** 1/3 matched

### 7. uapi.landlock_i686

- **Target:** `i686.LandlockI686`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 30910.0
- **Functions:** 0/3 matched
- **Missing functions:** `bindgen_test_layout_landlock_ruleset_attr`, `bindgen_test_layout_landlock_path_beneath_attr`, `bindgen_test_layout_landlock_net_port_attr`
- **Types:** 6/6 matched
- **Missing types:** _none_
- **Tests:** 0/3 matched

### 8. uapi.landlock_x86_64

- **Target:** `x8664.LandlockX8664`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 30910.0
- **Functions:** 0/3 matched
- **Missing functions:** `bindgen_test_layout_landlock_ruleset_attr`, `bindgen_test_layout_landlock_path_beneath_attr`, `bindgen_test_layout_landlock_net_port_attr`
- **Types:** 6/6 matched
- **Missing types:** _none_
- **Tests:** 0/3 matched

### 9. access

- **Target:** `landlock.Access`
- **Similarity:** 0.11
- **Dependents:** 0
- **Priority Score:** 20708.9
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
| `uapi.landlock_all` | `uapi.LandlockAll` | `uapi/landlock_all` |

### Missing

| Source | Expected target | Deps | Source path | Expected path |
|--------|-----------------|------|-------------|---------------|
| `lib` | `Lib` | 0 | `lib.rs` | `Lib.kt` |
| `uapi.mod` | `uapi.Mod` | 0 | `uapi/mod.rs` | `uapi/Mod.kt` |

