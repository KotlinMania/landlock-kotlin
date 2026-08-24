# port-lint Proposed Changes

**Generated:** 2026-08-24
**Source:** tmp/landlock
**Target:** src

These are review proposals only. They are emitted when a Rust -> Kotlin pair matches only after fallback normalization, so the existing `port-lint` header is not an exact provenance match.

| Target file | Current header | Proposed header | Source path | Reason |
|-------------|----------------|-----------------|-------------|--------|
| `commonMain/kotlin/io/github/kotlinmania/landlock/Scope.kt` | `// port-lint: source scope.rs` | `// port-lint: source scope.rs` | `scope.rs` | `port-lint provenance header matched only after fallback normalization: 'scope.rs' vs expected 'scope.rs'` |
| `commonMain/kotlin/io/github/kotlinmania/landlock/Ruleset.kt` | `// port-lint: source ruleset.rs` | `// port-lint: source ruleset.rs` | `ruleset.rs` | `port-lint provenance header matched only after fallback normalization: 'ruleset.rs' vs expected 'ruleset.rs'` |
| `commonMain/kotlin/io/github/kotlinmania/landlock/Compat.kt` | `// port-lint: source compat.rs` | `// port-lint: source compat.rs` | `compat.rs` | `port-lint provenance header matched only after fallback normalization: 'compat.rs' vs expected 'compat.rs'` |
| `commonMain/kotlin/io/github/kotlinmania/landlock/Fs.kt` | `// port-lint: source fs.rs` | `// port-lint: source fs.rs` | `fs.rs` | `port-lint provenance header matched only after fallback normalization: 'fs.rs' vs expected 'fs.rs'` |
| `commonMain/kotlin/io/github/kotlinmania/landlock/Net.kt` | `// port-lint: source net.rs` | `// port-lint: source net.rs` | `net.rs` | `port-lint provenance header matched only after fallback normalization: 'net.rs' vs expected 'net.rs'` |
| `commonMain/kotlin/io/github/kotlinmania/landlock/Errors.kt` | `// port-lint: source errors.rs` | `// port-lint: source errors.rs` | `errors.rs` | `port-lint provenance header matched only after fallback normalization: 'errors.rs' vs expected 'errors.rs'` |
| `commonMain/kotlin/io/github/kotlinmania/landlock/Access.kt` | `// port-lint: source access.rs` | `// port-lint: source access.rs` | `access.rs` | `port-lint provenance header matched only after fallback normalization: 'access.rs' vs expected 'access.rs'` |
| `commonMain/kotlin/io/github/kotlinmania/landlock/uapi/i686/LandlockI686.kt` | `// port-lint: source uapi/landlock_i686.rs` | `// port-lint: source uapi/landlock_i686.rs` | `uapi/landlock_i686.rs` | `port-lint provenance header matched only after fallback normalization: 'uapi/landlock_i686.rs' vs expected 'uapi/landlock_i686.rs'` |
| `commonMain/kotlin/io/github/kotlinmania/landlock/uapi/x8664/LandlockX8664.kt` | `// port-lint: source uapi/landlock_x86_64.rs` | `// port-lint: source uapi/landlock_x86_64.rs` | `uapi/landlock_x86_64.rs` | `port-lint provenance header matched only after fallback normalization: 'uapi/landlock_x86_64.rs' vs expected 'uapi/landlock_x86_64.rs'` |
| `commonMain/kotlin/io/github/kotlinmania/landlock/uapi/LandlockAll.kt` | `// port-lint: source uapi/landlock_all.rs` | `// port-lint: source uapi/landlock_all.rs` | `uapi/landlock_all.rs` | `port-lint provenance header matched only after fallback normalization: 'uapi/landlock_all.rs' vs expected 'uapi/landlock_all.rs'` |
| `commonTest/kotlin/io/github/kotlinmania/landlock/uapi/LandlockArchitectureBindingsTest.kt` | `// port-lint: tests uapi/landlock_all.rs` | `// port-lint: tests uapi/landlock_all.rs` | `uapi/landlock_all.rs` | `port-lint provenance header matched only after fallback normalization: 'tests:uapi/landlock_all.rs' vs expected 'uapi/landlock_all.rs'` |
