# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What This Project Is

MetaCode is a CLI code generator that produces working Spring applications (WebMvc, WebFlux, Spring Boot, Spring Batch) from the command line. The generated code includes unit tests, integration tests, and optional integrations (PostgreSQL, MongoDB, TestContainers, Liquibase, OpenAPI).

## Build Commands

```bash
# Build everything (also produces the shadow/uber jar)
./gradlew build

# Run all unit tests
./gradlew test

# Run integration tests only
./gradlew integrationTest

# Run both unit and integration tests (check depends on integrationTest)
./gradlew check

# Run tests for a single module
./gradlew :mc-common:test
./gradlew :mc-adapter-spring-core:integrationTest

# Run a single test class
./gradlew :mc-common:test --tests "mmm.coffee.metacode.common.catalog.CatalogFileReaderTest"

# Build the shadow jar for local testing (outputs to mc-application/build/libs/)
./gradlew :mc-application:shadowJar

# Check for dependency version updates
./gradlew dependencyUpdates
```

Java toolchain is set to Java 17.

## Module Structure

The project is organized as a Gradle multi-module build. The dependency graph flows from left to right:

```
mc-annotations
    ↑
mc-common ← mc-annotations
    ↑
mc-adapter-spring-shared ← mc-common
    ↑
mc-adapter-spring-core  ← mc-common, mc-adapter-spring-shared
    ↑
mc-adapter-spring-{webmvc,webflux,boot,batch} ← mc-adapter-spring-core
    ↑
mc-generator-spring ← all adapters + mc-common
    ↑
mc-application ← mc-generator-spring, mc-common, mc-adapter-spring-shared
```

**`mc-annotations`** — Custom annotations used across the project: Guice qualifier annotations (e.g. `@SpringWebMvc`, `@SpringWebFlux`) for Spring DI and `@ExcludeFromJacocoGeneratedReport` for coverage exclusions.

**`mc-common`** — Framework-agnostic core: template catalog infrastructure (`CatalogEntry`, `CatalogFileReader`), the `ICodeGenerator<T>` interface, FreeMarker template resolution, `MetaPropertiesHandler` for reading/writing `metacode.properties`, `IArchetypeDescriptorFactory`/`PackageLayout` for determining generated class names and package paths from TOML rules.

**`mc-adapter-spring-core`** — The concrete `SpringProjectCodeGenerator` that implements `ICodeGenerator<RestProjectDescriptor>`. Contains the project and endpoint template models (`RestProjectTemplateModel`), converters (descriptor → template model, descriptor → catalog predicate), and `MustacheDecoder` for resolving `{{token}}` expressions in output file destination paths.

**`mc-adapter-spring-{webmvc,webflux,boot,batch}`** — Each contains exactly one class: a `SpringXxxTemplateCatalog` that knows which YAML catalog files to load for that project type.

**`mc-generator-spring`** — Spring `@Configuration` class (`SpringGeneratorModule`) that wires all generators together. This is the Guice/Spring DI assembly point — each generator type gets its own `@Bean` with a qualifier annotation. Also contains `SpringGeneratorConfig` for component-scan configuration.

**`mc-application`** — PicoCLI + Spring Boot entry point. `MetaCodeCommand` is the root command; subcommands like `SubcommandCreateWebMvcProject` and `SubcommandCreateEndpoint` collect CLI args, build `RestProjectDescriptor`/`RestEndpointDescriptor`, then call the appropriate `ICodeGenerator`.

## Code Generation Pipeline

1. **CLI** parses args → builds a `RestProjectDescriptor` (for `create project`) or `RestEndpointDescriptor` (for `create endpoint`)
2. The appropriate `ICodeGenerator` is injected via Spring DI using qualifier annotations
3. `doPreprocessing()` writes `metacode.properties` to the target directory
4. `generateCode()` in `SpringProjectCodeGenerator`:
   - Builds a `RestProjectTemplateModel` (the FreeMarker data model)
   - Creates a `Predicate<CatalogEntry>` to filter which templates to render (based on integrations selected)
   - Configures `MustacheDecoder` with the template model to resolve `{{token}}` expressions in output paths
   - Iterates catalog entries, renders each FreeMarker `.ftl` template, resolves the output path, and writes the file

## Template System

Templates live in `mc-adapter-spring-core/src/main/resources/spring/templates/` (and similarly in other adapter modules for adapter-specific templates). They are FreeMarker (`.ftl`) files.

Catalog files (YAML, in `*/src/main/resources/spring/catalogs/`) declare which templates exist and where their output should go:
```yaml
entries:
  - scope: "project"        # "project" or "endpoint"
    archetype: "Controller" # maps to a generated class type
    facets:
      - facet: "main"
        sourceTemplate: "/spring/webmvc/SomeController.ftl"
        destination: "{{basePackagePath}}/endpoints/{{lowerCaseEntityName}}/api/{{entityName}}Controller.java"
```

Output destinations use `{{token}}` Mustache-style expressions resolved by `MustacheDecoder`. Token values come from `RestTemplateModelToMapConverter` or `RestEndpointTemplateModelToMapConverter`.

## Gradle Convention Plugins

Shared build logic lives in `gradle/plugins/conventions/src/main/kotlin/`:
- `buildlogic.java-common-conventions` — Java 17 toolchain, repositories
- `buildlogic.java-library-conventions` — extends common + `java-library` plugin
- `buildlogic.java-application-conventions` — extends common + `application` plugin
- `buildlogic.integration-test` — adds `integrationTest` source set, makes `check` depend on it
- `buildlogic.versioning` — reads version from `version.txt`

All library versions are centralized in `gradle/libs.versions.toml`.

## Testing Conventions

- Unit tests: `src/test/java` — use Mockito for mocking, AssertJ or Truth for assertions
- Integration tests: `src/integrationTest/java` — present only in modules that declare `id("buildlogic.integration-test")` in their `build.gradle.kts`
- `integrationTest` runs after `test` and is included in `check`
- Modules that don't have an `integrationTest` source set must explicitly set `sonar.tests` to only `src/test/java` to prevent Sonar errors
- `@ExcludeFromJacocoGeneratedReport` on a class excludes it from JaCoCo coverage; use this for DI wiring classes and POJOs

## Important Notes

- The `SpringGeneratorModule` comment warns: if you add a new `@Bean` provider, add a matching method to `SpringTestModule` (used by integration tests) or you will get `InitializationException` at runtime
- `RestProjectDescriptor.getFramework()` returns a string (not an enum) because FreeMarker templates compare against string values like `'WEBFLUX'`
- `commonsBeanUtils` has a TODO comment noting it hasn't been updated since 2019 and should be retired
- The shadow jar (`./gradlew :mc-application:shadowJar`) is used for local end-to-end testing; the `stage` task copies it to a scratch directory via `stageIt.sh`
