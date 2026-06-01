# landlock-kotlin in Kotlin

[![GitHub link](https://img.shields.io/badge/GitHub-KotlinMania%2Flandlock--kotlin-blue.svg)](https://github.com/KotlinMania/landlock-kotlin)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.kotlinmania/landlock-kotlin)](https://central.sonatype.com/artifact/io.github.kotlinmania/landlock-kotlin)
[![Build status](https://img.shields.io/github/actions/workflow/status/KotlinMania/landlock-kotlin/ci.yml?branch=main)](https://github.com/KotlinMania/landlock-kotlin/actions)

This is a Kotlin Multiplatform line-by-line transliteration port of [`landlock-lsm/rust-landlock`](https://github.com/landlock-lsm/rust-landlock).

**Original Project:** This port is based on [`landlock-lsm/rust-landlock`](https://github.com/landlock-lsm/rust-landlock). All design credit and project intent belong to the upstream authors; this repository is a faithful port to Kotlin Multiplatform with no behavioural changes intended.

### Porting status

This is an **in-progress port**. The goal is feature parity with the upstream Rust crate while providing a native Kotlin Multiplatform API. Every Kotlin file carries a `// port-lint: source <path>` header naming its upstream Rust counterpart so the AST-distance tool can track provenance.

---

## Upstream README — `landlock-lsm/rust-landlock`

> The text below is reproduced and lightly edited from [`https://github.com/landlock-lsm/rust-landlock`](https://github.com/landlock-lsm/rust-landlock). It is the upstream project's own description and remains under the upstream authors' authorship; links have been rewritten to absolute upstream URLs so they continue to resolve from this repository.

## Rust Landlock library

Landlock is a security feature available since Linux 5.13.
The goal is to enable to restrict ambient rights (e.g., global filesystem access) for a set of processes by creating safe security sandboxes as new security layers in addition to the existing system-wide access-controls.
This kind of sandbox is expected to help mitigate the security impact of bugs, unexpected or malicious behaviors in applications.
Landlock empowers any process, including unprivileged ones, to securely restrict themselves.
More information about Landlock can be found in the [official website](https://landlock.io).

This Rust crate provides a safe abstraction for the Landlock system calls along with some helpers.

## Use cases

This crate is especially useful to protect users' data by sandboxing:
* trusted applications dealing with potentially malicious data
  (e.g., complex file format, network request) that could exploit security vulnerabilities;
* sandbox managers, container runtimes or shells launching untrusted applications.

## Examples

A simple example can be found with the
[`path_beneath_rules()`](https://landlock.io/rust-landlock/landlock/fn.path_beneath_rules.html) helper.
More complex examples can be found with the
[`Ruleset` documentation](https://landlock.io/rust-landlock/landlock/struct.Ruleset.html)
and the [sandboxer example](https://github.com/landlock-lsm/rust-landlock/blob/HEAD/examples/sandboxer.rs).

## [Crate documentation](https://landlock.io/rust-landlock/landlock/)

## Changelog

* [v0.4.4](https://github.com/landlock-lsm/rust-landlock/blob/HEAD/CHANGELOG.md#v044)
* [v0.4.3](https://github.com/landlock-lsm/rust-landlock/blob/HEAD/CHANGELOG.md#v043)
* [v0.4.2](https://github.com/landlock-lsm/rust-landlock/blob/HEAD/CHANGELOG.md#v042)
* [v0.4.1](https://github.com/landlock-lsm/rust-landlock/blob/HEAD/CHANGELOG.md#v041)
* [v0.4.0](https://github.com/landlock-lsm/rust-landlock/blob/HEAD/CHANGELOG.md#v040)
* [v0.3.1](https://github.com/landlock-lsm/rust-landlock/blob/HEAD/CHANGELOG.md#v031)
* [v0.3.0](https://github.com/landlock-lsm/rust-landlock/blob/HEAD/CHANGELOG.md#v030)
* [v0.2.0](https://github.com/landlock-lsm/rust-landlock/blob/HEAD/CHANGELOG.md#v020)

---

## About this Kotlin port

### Installation

```kotlin
dependencies {
    implementation("io.github.kotlinmania:landlock-kotlin:0.1.2")
}
```

### Building

```bash
./gradlew build
./gradlew test
```

### Targets

- macOS arm64
- Linux x64
- Windows mingw-x64
- iOS arm64 / simulator-arm64 (Swift export + XCFramework)
- JS (browser + Node.js)
- Wasm-JS (browser + Node.js)
- Android (API 24+)

### Porting guidelines

See [AGENTS.md](AGENTS.md) and [CLAUDE.md](CLAUDE.md) for translator discipline, port-lint header convention, and Rust → Kotlin idiom mapping.

### License

This Kotlin port is distributed under the same MIT license as the upstream [`landlock-lsm/rust-landlock`](https://github.com/landlock-lsm/rust-landlock). See [LICENSE](LICENSE) (and any sibling `LICENSE-*` / `NOTICE` files mirrored from upstream) for the full text.

Original work copyrighted by the rust-landlock authors.  
Kotlin port: Copyright (c) 2026 Sydney Renee and The Solace Project.

### Acknowledgments

Thanks to the [`landlock-lsm/rust-landlock`](https://github.com/landlock-lsm/rust-landlock) maintainers and contributors for the original Rust implementation. This port reproduces their work in Kotlin Multiplatform; bug reports about upstream design or behavior should go to the upstream repository.
