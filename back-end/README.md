# Back-end

## Decisions and notes about this project

### Spring Batch

#### Is manually creating Spring Batch tables a good practice?

Manually creating Spring Batch tables is actually a common and valid approach in several scenarios.

##### Advantages:

1. **Database-specific optimizations**: Your SQL script adapts Spring Batch tables for SQLite, which has different data
   types and constraints than the standard schemas.
2. **Control over schema evolution**: You can manage schema versions explicitly through migration scripts.
3. **Integration with existing database migration frameworks**: Works well with tools like Flyway or Liquibase.
4. **Custom naming conventions or storage requirements**: Allows for organization-specific table designs.

##### Potential Concerns:

1. **Maintenance overhead**: You'll need to update your tables if a future Spring Batch version changes the schema.
2. **Version compatibility**: Ensuring your custom tables remain compatible with Spring Batch's expectations.

##### SQLite-specific Considerations

Your SQL script appears to use SQLite-specific syntax (e.g., `AUTOINCREMENT`). If you're using SQLite in production with
Spring Batch, be aware of some limitations:

- SQLite has limited concurrency support which might affect batch job execution in multi-instance environments.
- SQLite's transaction isolation may differ from other databases Spring Batch typically runs on.

##### About reviewing SQL Schema Completeness

Your schema looks comprehensive, but verify it includes all required columns from the latest Spring Batch version you're
using. The standard schemas are available in the Spring Batch repository and might be worth comparing against.

## TODO

### OAuth2

- Resources to check eventually
    - https://www.youtube.com/watch?v=7zm3mxaAFWk
- Look at how Immich has done Oauth2 (they're a client only I believe):
    - https://github.com/immich-app/immich/issues/33
    - https://github.com/immich-app/immich/pull/884
- On OAuth2 done wrong:
    - http://blog.intothesymmetry.com/2015/06/on-oauth-token-hijacks-for-fun-and.html

### General

- Any videos from these people are good
    - [Dan Vega](https://www.youtube.com/@DanVega/videos)
    - Gabriel Moiroux
    - Laurentiu Splica
    - Vlad Mihalcea
    - Thorbenn Jansen
    - [Coffee + Software](https://www.youtube.com/@coffeesoftware/videos)

## Info

### Spring Security

- Tip: the order of Spring Security filters can be found in the class
  ` org.springframework.security.config.annotation.web.builders.FilterOrderRegistration`.
- Trying to follow this guide at the moment https://spring.io/guides/tutorials/spring-security-and-angular-js

### Sessions

- For Spring Session with Redis, see: https://docs.spring.io/spring-session/reference/getting-started/using-redis.html
- Official docs for session management
    - https://docs.spring.io/spring-security/reference/servlet/authentication/persistence.html
    - https://docs.spring.io/spring-security/reference/servlet/authentication/session-management.html

### JPA/Hibernate

- Read "Java Persistence with Hibernate", published by Manning. Maybe there is a new edition, "Java Persistence with
  Spring Data".
