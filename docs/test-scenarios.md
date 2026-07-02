# Dependabot PR Triage Test Scenarios

This document defines the comprehensive test scenarios for verifying the Dependabot PR Triage workflow (`dependabot-pr-triage.md`).

The project intentionally uses old library versions to enable realistic Dependabot PR generation and AI agent triage validation.

---

## Overview

The Dependabot PR Triage workflow makes two independent decisions for each PR:

1. **Merge Decision**: Whether the dependency update can be merged without source code changes
   - Options: `Merge Recommended` / `Conditional Hold` / `Merge Blocked`
2. **Incident Decision**: Whether the old version likely exposed sensitive data or enabled abuse
   - Options: `Incident Review Required` / `Not Required`

This document describes five test scenarios, each targeting a distinct combination of merge/incident decisions.

---

## Scenario 1: Apache Commons Lang 3.x → 4.x (Breaking Changes)

### Library Information

- **Current Version**: 3.12.0 (in `gradle/libs.versions.toml`)
- **Target Version**: 4.0.0+ (e.g., 4.1.0)
- **Module**: `org.apache.commons:commons-lang3`

### Test Case Objective

Verify that the Dependabot PR Triage workflow correctly identifies breaking API changes that require source code modifications.

### Codebase Usage

- **Implementation**: [src/main/kotlin/com/example/ghas/DependencyUsageExamples.kt](../src/main/kotlin/com/example/ghas/DependencyUsageExamples.kt#L29-L46)
  - Uses: `StringUtils.isBlank()`, `StringUtils` API consistency
  - Rationale: StringUtils API is stable across versions, but major changes exist in other areas (e.g., CharRange removal)
- **Test Coverage**: [src/test/kotlin/com/example/ghas/DependencyUsageExamplesTest.kt](../src/test/kotlin/com/example/ghas/DependencyUsageExamplesTest.kt#L19-L25)

### Dependabot PR Characteristics

- **PR Body**: Will indicate "major version update" with breaking changes mentioned
- **Compatibility**: Full recompile required; some APIs removed between 3.x and 4.x
- **Changed Files**: Only `gradle/libs.versions.toml` and/or `build.gradle.kts` (dependency files)

### Expected Triage Decisions

| Decision Axis | Expected Decision | Rationale |
|---|---|---|
| **Merge Decision** | `Merge Blocked` | Major version upgrade introduces breaking API changes; application code must be updated to maintain compatibility |
| **Incident Decision** | `Not Required` | No known security vulnerabilities addressed; only API design improvements |

### Verification Steps

1. Update `gradle/libs.versions.toml`: Change `commonsLang = "3.12.0"` to `"4.1.0"` (or latest 4.x)
2. Wait for Dependabot to create a PR (or trigger manually via workflow dispatch)
3. Verify PR Comment includes:
   - Merge Decision: `Merge Blocked` with rationale referencing API incompatibility
   - Incident Decision: `Not Required`
   - Evidence: Links to changed files and code usage examples

### Impact Analysis

- ✗ **No direct compilation error** from StringUtils usage (compatible method exists in both versions)
- ✗ **Compilation may fail** in other parts of codebase if specific 3.x-only APIs are used
- ✓ **Version conflict** evident in dependency tree or CI logs

---

## Scenario 2: Log4j 1.2.17 (Known Vulnerability - Log4Shell)

### Library Information

- **Current Version**: 1.2.17 (in `gradle/libs.versions.toml`)
- **Target Version**: 2.17.1+ (or 2.x series)
- **Module**: `log4j:log4j`
- **Known Vulnerability**: CVE-2021-44228 (Log4Shell), CVE-2021-44832, and others

### Test Case Objective

Verify that the Dependabot PR Triage workflow correctly identifies high-risk security updates and recommends incident investigation.

### Codebase Usage

- **Implementation**: [src/main/kotlin/com/example/ghas/DependencyUsageExamples.kt](../src/main/kotlin/com/example/ghas/DependencyUsageExamples.kt#L55-L72)
  - Uses: `Logger.getLogger()`, `logger.info()`, `logger.warn()`
  - Rationale: Demonstrates use of old Log4j 1.2.x API
- **Test Coverage**: [src/test/kotlin/com/example/ghas/DependencyUsageExamplesTest.kt](../src/test/kotlin/com/example/ghas/DependencyUsageExamplesTest.kt#L30-L36)

### Dependabot PR Characteristics

- **PR Body**: Will indicate multiple known vulnerabilities (CVE numbers) with high/critical severity
- **Advisories**: GitHub Advisory Database lists widespread exploitation and data exposure risks
- **Urgency**: Marked as critical/high priority

### Expected Triage Decisions

| Decision Axis | Expected Decision | Rationale |
|---|---|---|
| **Merge Decision** | `Conditional Hold` or `Merge Blocked` | Major version jump (1.x → 2.x) with significant API changes; may require source code migration |
| **Incident Decision** | `Incident Review Required` | CVE-2021-44228 (Log4Shell) and related CVEs represent critical vulnerabilities; exploitation is likely under the old version if application accepts untrusted input |

### Verification Steps

1. Update `gradle/libs.versions.toml`: Change `log4j = "1.2.17"` to `"2.17.1"` or higher
2. Wait for Dependabot PR (or trigger manually)
3. Verify PR Comment includes:
   - Merge Decision: `Conditional Hold` or `Merge Blocked` with API migration notes
   - Incident Decision: `Incident Review Required`
   - Evidence: CVE links, vulnerability descriptions, high/critical severity indicators

### Impact Analysis

- ✓ **High-risk exposure confirmed** by CVE database and advisories
- ✓ **Reachability clear**: Logger is used throughout the application
- ✓ **Urgent action required**: Dependency update should be prioritized for security hardening

---

## Scenario 3: Gson 2.8.9 (Deprecated API Usage)

### Library Information

- **Current Version**: 2.8.9 (in `gradle/libs.versions.toml`)
- **Target Version**: 2.10.0+ (e.g., 2.10.1)
- **Module**: `com.google.code.gson:gson`

### Test Case Objective

Verify that the Dependabot PR Triage workflow correctly identifies API deprecation and recommends verification before merging.

### Codebase Usage

- **Implementation**: [src/main/kotlin/com/example/ghas/DependencyUsageExamples.kt](../src/main/kotlin/com/example/ghas/DependencyUsageExamples.kt#L85-L112)
  - Uses: `Gson()`, `gson.toJson()`, `gson.fromJson()`
  - Rationale: Core Gson API; stable across minor versions but may have deprecation warnings in newer releases
- **Test Coverage**: [src/test/kotlin/com/example/ghas/DependencyUsageExamplesTest.kt](../src/test/kotlin/com/example/ghas/DependencyUsageExamplesTest.kt#L41-L47)

### Dependabot PR Characteristics

- **PR Body**: May indicate minor version update with deprecation notes
- **Compatibility**: Backward compatible; existing code continues to function
- **Warnings**: Optional deprecation warnings in newer versions; no breaking changes

### Expected Triage Decisions

| Decision Axis | Expected Decision | Rationale |
|---|---|---|
| **Merge Decision** | `Conditional Hold` | Minor version upgrade within 2.x series; backward compatible but review recommended to confirm no deprecated APIs are used or plan migration |
| **Incident Decision** | `Not Required` | No known security vulnerabilities in the upgrade path; primarily API evolution and best practices |

### Verification Steps

1. Update `gradle/libs.versions.toml`: Change `gson = "2.8.9"` to `"2.10.1"` or similar
2. Wait for Dependabot PR
3. Verify PR Comment includes:
   - Merge Decision: `Conditional Hold` with note about deprecation review
   - Incident Decision: `Not Required`
   - Evidence: Release notes mentioning deprecations and compatibility notes

### Impact Analysis

- ✓ **Code compiles successfully** with new version
- ✓ **Functionality preserved**: Core JSON operations unchanged
- ✗ **Deprecation warnings possible** in IDE or compiler output; not blocking

---

## Scenario 4: OkHttp 3.14.9 (Security Patch / Minor Version)

### Library Information

- **Current Version**: 3.14.9 (in `gradle/libs.versions.toml`)
- **Target Version**: 3.14.10+ (patch release) or 4.x (if available)
- **Module**: `com.squareup.okhttp3:okhttp`

### Test Case Objective

Verify that the Dependabot PR Triage workflow correctly identifies safe patch/minor updates that can be merged without hesitation.

### Codebase Usage

- **Implementation**: [src/main/kotlin/com/example/ghas/DependencyUsageExamples.kt](../src/main/kotlin/com/example/ghas/DependencyUsageExamples.kt#L125-L147)
  - Uses: `OkHttpClient()`, `Request.Builder()`, `newCall().execute()`
  - Rationale: Standard HTTP client usage; highly stable across patch versions
- **Test Coverage**: [src/test/kotlin/com/example/ghas/DependencyUsageExamplesTest.kt](../src/test/kotlin/com/example/ghas/DependencyUsageExamplesTest.kt#L52-L57)

### Dependabot PR Characteristics

- **PR Body**: Indicates patch or minor version update with security and stability fixes
- **Compatibility**: Fully backward compatible within 3.14.x series
- **Changes**: Bug fixes, security patches; no API changes

### Expected Triage Decisions

| Decision Axis | Expected Decision | Rationale |
|---|---|---|
| **Merge Decision** | `Merge Recommended` | Patch/minor version within stable 3.x series; no breaking changes; security improvements justify immediate merge |
| **Incident Decision** | `Not Required` | Routine patch release; no known vulnerabilities targeted (only fixes applied) |

### Verification Steps

1. Update `gradle/libs.versions.toml`: Change `okhttp = "3.14.9"` to `"3.14.10"` or similar patch
2. Wait for Dependabot PR
3. Verify PR Comment includes:
   - Merge Decision: `Merge Recommended`
   - Incident Decision: `Not Required`
   - Evidence: Release notes confirming patch-only changes, backward compatibility

### Impact Analysis

- ✓ **Zero breaking changes**: Patch release within stable series
- ✓ **Test suite passes**: All HTTP client operations continue to work unchanged
- ✓ **Safe to merge immediately**: No validation or source changes required

---

## Scenario 5: Apache HttpClient 4.5.13 (Minor Version Compatibility)

### Library Information

- **Current Version**: 4.5.13 (in `gradle/libs.versions.toml`)
- **Target Version**: 4.5.14+ (e.g., 4.5.15)
- **Module**: `org.apache.httpcomponents:httpclient`

### Test Case Objective

Verify that the Dependabot PR Triage workflow correctly identifies stable minor version updates within a mature series that require routine, low-risk validation.

### Codebase Usage

- **Implementation**: [src/main/kotlin/com/example/ghas/DependencyUsageExamples.kt](../src/main/kotlin/com/example/ghas/DependencyUsageExamples.kt#L160-L184)
  - Uses: `HttpClients.createDefault()`, `HttpGet()`, `execute()`, `getStatusLine()`
  - Rationale: Standard Apache HTTP client usage; ubiquitous in enterprise Java
- **Test Coverage**: [src/test/kotlin/com/example/ghas/DependencyUsageExamplesTest.kt](../src/test/kotlin/com/example/ghas/DependencyUsageExamplesTest.kt#L62-L68)

### Dependabot PR Characteristics

- **PR Body**: Indicates minor version maintenance release within 4.5.x series
- **Compatibility**: Fully backward compatible; 4.5.x is a stable, long-term maintenance branch
- **Release Type**: Routine maintenance; incremental stability improvements

### Expected Triage Decisions

| Decision Axis | Expected Decision | Rationale |
|---|---|---|
| **Merge Decision** | `Merge Recommended` | Stable minor version within 4.5.x maintenance series; full backward compatibility; safe to merge after routine verification |
| **Incident Decision** | `Not Required` | Routine maintenance release; no known critical vulnerabilities addressed in this minor version bump |

### Verification Steps

1. Update `gradle/libs.versions.toml`: Change `httpClient = "4.5.13"` to `"4.5.15"` or similar
2. Wait for Dependabot PR
3. Verify PR Comment includes:
   - Merge Decision: `Merge Recommended`
   - Incident Decision: `Not Required`
   - Evidence: Release notes indicating maintenance/stability focus, backward compatibility

### Impact Analysis

- ✓ **Complete backward compatibility**: HttpClient 4.5.x series emphasizes stability
- ✓ **No API changes**: Core methods (`execute()`, `getStatusLine()`, etc.) remain identical
- ✓ **Routine CI/CD flow**: Standard test execution confirms continued functionality

---

## Test Execution Checklist

For each scenario, follow these verification steps:

### Phase 1: Dependabot PR Generation

- [ ] Update `gradle/libs.versions.toml` with new version
- [ ] Commit and push changes (or trigger Dependabot manually)
- [ ] Confirm Dependabot PR appears within expected timeframe

### Phase 2: Triage Workflow Execution

- [ ] Verify `.github/workflows/dependabot-pr-triage.md` workflow is triggered on PR
- [ ] Wait for AI agent (`Copilot`) to analyze and post comment
- [ ] Confirm PR comment includes all required sections

### Phase 3: Decision Validation

- [ ] Check Merge Decision matches expected outcome
- [ ] Check Incident Decision matches expected outcome
- [ ] Verify Evidence Links section references relevant code and CVEs
- [ ] Confirm Labels reflect merge decision (e.g., `merge-recommended` or `merge-blocked`)
- [ ] Verify Check Run status aligns with merge decision

### Phase 4: Optional - Manual Build Verification

- [ ] Run `gradle clean build` with updated version
- [ ] Confirm compilation success/failure matches expectations
- [ ] Verify test execution results (`testImplementation` tests pass)

---

## Summary Table

| Scenario | Library | Current → Target | Merge Decision | Incident Decision | Rationale |
|---|---|---|---|---|---|
| 1 | Commons Lang | 3.12.0 → 4.x | **Merge Blocked** | Not Required | Breaking API changes require source updates |
| 2 | Log4j | 1.2.17 → 2.17.1+ | **Conditional Hold** | **Incident Review Required** | Critical CVE-2021-44228 vulnerability; urgent security update |
| 3 | Gson | 2.8.9 → 2.10.x | **Conditional Hold** | Not Required | Deprecation warnings; compatibility maintained |
| 4 | OkHttp | 3.14.9 → 3.14.10+ | **Merge Recommended** | Not Required | Patch release; safe minor version within series |
| 5 | HttpClient | 4.5.13 → 4.5.15+ | **Merge Recommended** | Not Required | Stable maintenance release; routine update |

---

## Additional Notes

- **Dependabot Scheduling**: Adjust `.github/dependabot.yml` to control PR frequency during testing
- **GitHub Advisory Database**: Scenarios rely on accurate CVE mappings in GitHub's advisory data
- **Network Requests**: OkHttp and HttpClient scenarios execute actual HTTP calls; ensure network connectivity during testing
- **Gradle Cache**: Run `gradle clean` before each test to avoid cached dependency resolution issues
- **CI Logs**: Check `github-runner` logs for detailed compile/test output if verification fails

---

## Reference Documentation

- [Dependabot PR Triage Workflow](./.github/workflows/dependabot-pr-triage.md)
- [GHAS Verification Guide](./ghas-verification-guide.md)
- [Triage Playbook](./triage-playbook.md)
- [GitHub Advisory Database](https://github.com/advisories)
