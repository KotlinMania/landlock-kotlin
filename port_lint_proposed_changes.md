# port-lint Proposed Changes

**Generated:** 2026-08-31
**Source:** tmp/landlock/src
**Target:** src/commonMain/kotlin/io/github/kotlinmania/landlock

These are review proposals only. They are emitted when a Rust -> Kotlin pair matches only after fallback normalization, so the existing `port-lint` header is not an exact provenance match.

| Target file | Current header | Proposed header | Source path | Reason |
|-------------|----------------|-----------------|-------------|--------|
| `src/commonMain/kotlin/io/github/kotlinmania/landlock/Scope.kt` | `// port-lint: source landlock/src/scope.rs` | `// port-lint: source scope.rs` | `scope.rs` | `port-lint provenance header matched only after fallback normalization: 'landlock/src/scope.rs' vs expected 'scope.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/landlock/Ruleset.kt` | `// port-lint: source landlock/src/ruleset.rs` | `// port-lint: source ruleset.rs` | `ruleset.rs` | `port-lint provenance header matched only after fallback normalization: 'landlock/src/ruleset.rs' vs expected 'ruleset.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/landlock/Compat.kt` | `// port-lint: source landlock/src/compat.rs` | `// port-lint: source compat.rs` | `compat.rs` | `port-lint provenance header matched only after fallback normalization: 'landlock/src/compat.rs' vs expected 'compat.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/landlock/CompatTest.kt` | `// port-lint: tests landlock/src/compat.rs` | `// port-lint: tests compat.rs` | `compat.rs` | `port-lint provenance header matched only after fallback normalization: 'tests:landlock/src/compat.rs' vs expected 'compat.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/landlock/Fs.kt` | `// port-lint: source landlock/src/fs.rs` | `// port-lint: source fs.rs` | `fs.rs` | `port-lint provenance header matched only after fallback normalization: 'landlock/src/fs.rs' vs expected 'fs.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/landlock/Lib.kt` | `// port-lint: source landlock/src/lib.rs` | `// port-lint: source lib.rs` | `lib.rs` | `port-lint provenance header matched only after fallback normalization: 'landlock/src/lib.rs' vs expected 'lib.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/landlock/LandlockTest.kt` | `// port-lint: tests landlock/src/lib.rs` | `// port-lint: tests lib.rs` | `lib.rs` | `port-lint provenance header matched only after fallback normalization: 'tests:landlock/src/lib.rs' vs expected 'lib.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/landlock/Net.kt` | `// port-lint: source landlock/src/net.rs` | `// port-lint: source net.rs` | `net.rs` | `port-lint provenance header matched only after fallback normalization: 'landlock/src/net.rs' vs expected 'net.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/landlock/Errors.kt` | `// port-lint: source landlock/src/errors.rs` | `// port-lint: source errors.rs` | `errors.rs` | `port-lint provenance header matched only after fallback normalization: 'landlock/src/errors.rs' vs expected 'errors.rs'` |
| `src/commonTest/kotlin/io/github/kotlinmania/landlock/ErrorsTest.kt` | `// port-lint: tests landlock/src/errors.rs` | `// port-lint: tests errors.rs` | `errors.rs` | `port-lint provenance header matched only after fallback normalization: 'tests:landlock/src/errors.rs' vs expected 'errors.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/landlock/Access.kt` | `// port-lint: source landlock/src/access.rs` | `// port-lint: source access.rs` | `access.rs` | `port-lint provenance header matched only after fallback normalization: 'landlock/src/access.rs' vs expected 'access.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/landlock/uapi/i686/LandlockI686.kt` | `// port-lint: source landlock/src/uapi/landlock_i686.rs` | `// port-lint: source uapi/landlock_i686.rs` | `uapi/landlock_i686.rs` | `port-lint provenance header matched only after fallback normalization: 'landlock/src/uapi/landlock_i686.rs' vs expected 'uapi/landlock_i686.rs'` |
| `src/commonMain/kotlin/io/github/kotlinmania/landlock/uapi/x8664/LandlockX8664.kt` | `// port-lint: source landlock/src/uapi/landlock_x86_64.rs` | `// port-lint: source uapi/landlock_x86_64.rs` | `uapi/landlock_x86_64.rs` | `port-lint provenance header matched only after fallback normalization: 'landlock/src/uapi/landlock_x86_64.rs' vs expected 'uapi/landlock_x86_64.rs'` |
