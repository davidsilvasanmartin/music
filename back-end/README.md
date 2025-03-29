Back-end

# Decisions, notes about this project, in-depth explanations

Here there are in-depth explanations for several topics

- [Spring Batch](docs/spring-batch.md)
- [Spring/JPA Repositories](docs/repositories.md)

# TODO

## OAuth2

- Resources to check eventually
    - https://www.youtube.com/watch?v=7zm3mxaAFWk
- Look at how Immich has done Oauth2 (they're a client only I believe):
    - https://github.com/immich-app/immich/issues/33
    - https://github.com/immich-app/immich/pull/884
- On OAuth2 done wrong:
    - http://blog.intothesymmetry.com/2015/06/on-oauth-token-hijacks-for-fun-and.html

# Info, resources, tutorials, etc.

- Any videos from these people are good
    - [Dan Vega](https://www.youtube.com/@DanVega/videos)
    - Gabriel Moiroux
    - Laurentiu Splica
    - Vlad Mihalcea
    - Thorbenn Jansen
    - [Coffee + Software](https://www.youtube.com/@coffeesoftware/videos)

## Spring Security

- Tip: the order of Spring Security filters can be found in the class
  ` org.springframework.security.config.annotation.web.builders.FilterOrderRegistration`.
- Trying to follow this guide at the moment https://spring.io/guides/tutorials/spring-security-and-angular-js

## Sessions

- For Spring Session with Redis, see: https://docs.spring.io/spring-session/reference/getting-started/using-redis.html
- Official docs for session management
    - https://docs.spring.io/spring-security/reference/servlet/authentication/persistence.html
    - https://docs.spring.io/spring-security/reference/servlet/authentication/session-management.html

## JPA/Hibernate

- Read "Java Persistence with Hibernate", published by Manning. Maybe there is a new edition, "Java Persistence with
  Spring Data".
