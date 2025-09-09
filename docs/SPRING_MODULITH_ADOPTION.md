# Assessing Spring Modulith for the /back-end project

Date: 2025-08-11

This document evaluates whether the `/back-end` service (package root `dev.davidsilva.music`) is a good candidate to adopt Spring Modulith, outlines pros and cons, and proposes a detailed, incremental refactoring plan.

Note: This assessment is based on the observed structure and classes referenced in the codebase, including packages such as `album`, `artist`, `song`, `genre`, `playlist`, `search`, `job`, and shared configuration (`AppDbConfiguration`) and specifications (`AbstractSpecification`, `AlbumSpecification`).

## What is Spring Modulith (in brief)

Spring Modulith is an architectural support library for building modular monoliths with Spring Boot. It helps:
- Define modules explicitly and enforce boundaries at build/test-time.
- Separate internal vs exported APIs using annotations (`@ApplicationModule`, `@NamedInterface`).
- Model inter-module communication via events, with powerful testing support.
- Generate architecture documentation and visualize module graphs.

## Is the `/back-end` a good candidate?

Short answer: Yes, it appears to be a strong candidate for Spring Modulith.

Why:
- The codebase is already package-by-feature oriented (e.g., `album`, `artist`, `song`, `genre`, `playlist`, `search`, `job`). These align naturally with module boundaries.
- The domain aggregates are relatively clear (Albums contain Songs; Artists relate to Albums/Songs; Genres tag Albums/Songs; Playlists aggregate Songs).
- Presence of Specifications (`AbstractSpecification`, `AlbumSpecification`) suggests some domain-oriented query encapsulation and separation of concerns already underway.
- The `job` package (e.g., `ImportAlbumsJob`, `JobController`) is a separate concern that could cleanly become a module with event-driven interactions.
- Likely usage of Spring Data JPA, REST controllers, DTOs (e.g., `GenreDto`) — a standard Spring Boot stack that Spring Modulith integrates with seamlessly.

Caveats:
- If there are strong cyclic dependencies across packages (common in monoliths), some refactoring will be necessary to break cycles.
- If controllers or services directly reach across features (e.g., AlbumController directly manipulating Playlist internals), those must be mediated via exported interfaces or domain events.

## Pros and Cons of adopting Spring Modulith

### Pros
- Clear module boundaries: Enforce package-level architectural rules using `@ApplicationModule`, `@NamedInterface`, and `ApplicationModules` tests.
- Incremental adoption: Can be introduced one module at a time without a big-bang rewrite.
- Better maintainability: Internal details hidden; only exported APIs are accessible, reducing accidental coupling.
- Event-driven collaboration: Domain/application events encourage decoupling (e.g., when albums/songs are imported, search indexing or playlist recommendations react asynchronously).
- Architecture documentation: Built-in documentation and module graph visibility; helps onboarding and architectural reviews.
- Test support: Module slice tests and architectural rule tests catch boundary violations early.
- Future-proofing: Eases eventual extraction of modules into services, if ever needed, by first getting boundaries right inside the monolith.

### Cons / Risks
- Learning curve: Team needs to learn Spring Modulith annotations, patterns, and tests.
- Initial refactor cost: Moving packages, introducing exported interfaces, and fixing direct cross-module calls takes effort.
- Eventing complexity: Introducing events can complicate transactional boundaries and consistency if done prematurely.
- Tooling changes: Requires Spring Boot 3.1+ (preferably 3.2+) and Java 17+, dependency updates, and build changes.
- Possible short-term slowdown: Boundary enforcement may initially slow development as violations are fixed.
- Over-segmentation risk: Too many tiny modules can increase friction; module granularity must be chosen carefully.

## Proposed target module map

Initial module boundaries (suggested):

- artist
  - Domain: `Artist`
  - API: queries by name, CRUD operations
- album
  - Domain: `Album`, aggregates `Song` references; `AlbumSpecification`
  - API: album queries (by artist, year, genre), album CRUD
- song
  - Domain: `Song`
  - API: song queries, CRUD, audio metadata updates
- genre
  - Domain: `Genre`, `GenreDto`
  - API: genre listing & management
- playlist
  - Domain: playlist aggregates (`Playlist`, `PlaylistItem`)
  - API: playlist CRUD, append/remove songs
- search
  - Application: generic search endpoints and specification integration (`AbstractSpecification`), possibly adapter for search UI
  - API: search queries across modules via exported facade
- job
  - Application: `ImportAlbumsJob`, `JobController` to trigger jobs
  - API: job scheduling/triggering; emits domain/application events (e.g., AlbumImported, LibraryScanned)
- shared (minimize; avoid a “junk drawer”)
  - Cross-cutting types that are truly generic (e.g., error handling, common infrastructure configuration like `AppDbConfiguration` if not module-specific)

Notes:
- Keep bi-directional relationships (e.g., album <-> song) encapsulated within a single module where possible; expose capabilities via exported services rather than exposing entities across modules.
- DTOs should be per-module API specifics; avoid a large shared DTO package.

## Refactoring plan (incremental)

The plan is designed to be low-risk and iterative.

1. Baseline and prerequisites
   - Ensure Java 17+ and Spring Boot 3.2.x (or later).
   - Add dependencies (Maven):
     ```xml
     <dependency>
       <groupId>org.springframework.modulith</groupId>
       <artifactId>spring-modulith-starter</artifactId>
     </dependency>
     <!-- Optional: actuator support for module graph -->
     <dependency>
       <groupId>org.springframework.modulith</groupId>
       <artifactId>spring-modulith-actuator</artifactId>
     </dependency>
     ```
     Or Gradle:
     ```gradle
     implementation("org.springframework.modulith:spring-modulith-starter")
     runtimeOnly("org.springframework.modulith:spring-modulith-actuator") // optional
     ```
   - Enable the Modulith application if desired (optional in newer versions):
     ```java
     // @SpringBootApplication
     // @EnableModulith // optional depending on version
     ```

2. Identify and declare modules
   - In the root package `dev.davidsilva.music`, create package-info.java with `@ApplicationModule` per subpackage or add `@ApplicationModule` in classes representing module roots.
   - Within each module, define exported APIs using `@NamedInterface` on API packages (e.g., `api`, `application`, or `facade`). Keep `domain` and `internal` packages unexported by default.

3. Restructure by feature
   - Ensure the following internal folder structure per module (example for album):
     - `dev.davidsilva.music.album.domain` (entities, repository interfaces, domain services)
     - `dev.davidsilva.music.album.application` (use cases, transactional services)
     - `dev.davidsilva.music.album.api` (controllers/rest DTOs or exported facades)
     - `dev.davidsilva.music.album.infrastructure` (adapters, persistence, mappers if needed)
   - Move existing classes accordingly:
     - `AlbumSpecification` → `album.domain` (or `album.application` if it orchestrates queries)
     - `Song` → `song.domain`
     - `Artist` → `artist.domain`
     - `GenreDto` → `genre.api` (and map to domain `Genre`)
     - `ImportAlbumsJob`, `JobController` → `job.application` and `job.api`
     - `AbstractSpecification` → if generic, `search.domain` or `shared.domain` (prefer search)
     - `AppDbConfiguration` → `shared.infrastructure` unless clearly module-specific

4. Introduce exported interfaces and replace direct cross-module calls
   - For cross-module needs (e.g., playlists needing song info):
     - Define an exported facade in the owning module (`song.api.SongQueryFacade`).
     - Depend on the facade interface (not concrete classes) from other modules.
   - Avoid exposing JPA entities across modules; instead expose DTOs or read-only projections.

5. Model inter-module events where beneficial
   - Use domain/application events to decouple workflows:
     - When `job` imports albums and songs, publish `AlbumImportedEvent`/`SongImportedEvent`.
     - `search` module listens to these events to update search indices.
     - `playlist` module might listen for `SongDeletedEvent` to clean up items.
   - Use Spring Modulith’s event publication and testing support. Consider `@TransactionalEventListener` when needed for transactional semantics.

6. Add architecture tests to enforce boundaries
   - Create a test class under `/back-end/src/test/java`:
     ```java
     @SpringBootTest
     class ArchitectureTests {
       @Test
       void modules_should_be_consistent() {
         ApplicationModules modules = ApplicationModules.of(dev.davidsilva.music.class);
         modules.verify(); // fails on illegal dependencies
       }
     }
     ```
   - Add explicit rules if needed (e.g., `album` may depend on `artist`, but not vice versa).

7. Controller adaptation and API stability
   - Controllers live in each module’s `api` package.
   - Keep existing endpoint contracts stable; adapt internals to call application services or published facades.
   - Map module-internal entities to API DTOs; do not leak entities out of module.

8. Repository and transaction boundaries
   - Repositories remain inside their owning module.
   - Use application-layer services as transactional boundaries (`@Transactional`).
   - When multiple modules are involved, prefer orchestration in the caller’s application layer via exported facades, or use events.

9. Observability and documentation
   - If using `spring-modulith-actuator`, expose the module graph and document it.
   - Generate snippets of the module architecture and include in developer docs.

10. Incremental rollout order
   - Phase 1: genre, artist, song (usually lower coupling) — establish patterns.
   - Phase 2: album (ties together artist and song) — validate inter-module calls and events.
   - Phase 3: playlist (depends on song) — refactor to use `SongQueryFacade` or events.
   - Phase 4: search and job — finalize event-driven flows (import → index).
   - Phase 5: hardening — boundary tests, cleanup, documentation, and performance checks.

11. Risks and mitigations
   - Cyclic dependencies: Use facades and events; split utilities that create cycles; move truly cross-cutting code to a small `shared`.
   - Performance regressions from extra indirections: Measure critical paths; keep synchronous calls for hot paths; use events for async consistency where appropriate.
   - Test brittleness: Add module-level slice and architecture tests early to guide refactoring.

12. Acceptance criteria
   - ApplicationModules.verify() passes with defined module rules.
   - No direct compile-time dependencies from one module’s internal packages to another’s internal packages.
   - Public REST API remains backward compatible.
   - Key workflows (album import, album search, playlist management) function with module boundaries and, where used, events.

## Quick checklist to get started
- [ ] Upgrade to Boot 3.2+ and Java 17+ (if not already).
- [ ] Add Spring Modulith dependencies.
- [ ] Annotate module roots (`@ApplicationModule`) and define exported interfaces (`@NamedInterface`).
- [ ] Create ApplicationModules architecture test and make it pass.
- [ ] Migrate one module at a time following the order above.
- [ ] Introduce events selectively where they reduce coupling.
- [ ] Document the module graph via actuator or generated docs.

## Conclusion
The `/back-end` service’s current feature-oriented packaging and well-defined domain areas make it a solid candidate for Spring Modulith. With an incremental, test-driven refactoring, the team can gain clearer boundaries, improved maintainability, and optional event-driven collaboration without the overhead of moving to microservices.
