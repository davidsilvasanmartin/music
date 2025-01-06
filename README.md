# Music

This project is a Plex-like website that works with Beets

## Usage tips

- On MAC, in order to be able to play hi-fi audio, we need to enable it as per the following
  article: https://support.apple.com/en-us/101405. However, it is still not clear to me whether the
  default HTML audio player will use the enhanced quality or not.

## `/music`

Folder that contains the Beets configuration. Use the project within this folder to create the music library.

## `/back-end`

Spring application running on port 8080.

Selected topics:

- JPA Criteria queries
    - https://www.baeldung.com/jpa-and-or-criteria-predicates
    - https://www.baeldung.com/spring-data-criteria-queries
    - https://www.baeldung.com/hibernate-criteria-queries
    - https://www.baeldung.com/rest-api-search-language-spring-data-specifications
- Configuring 2 different datasources
    - I have followed this
      tutorial https://springframework.guru/how-to-configure-multiple-data-sources-in-a-spring-boot-application/
    - Please note: I had to annotate all services as Transactional because otherwise I was getting this
      error: https://stackoverflow.com/questions/21574236/
- SQLITE configuration in Spring Boot
    - https://www.baeldung.com/spring-boot-sqlite
    - When configuring the dialect with Java, we should use `hibernate.dialect` instead of the
      `spring.jpa.database-platform`
      property which is used in the `application.properties`
      file: https://stackoverflow.com/questions/67839078/how-to-configure-multiple-database-platforms-in-spring-boot
- Authentication and authorization with JWT
    - https://chariotsolutions.com/blog/post/angular-2-spring-boot-jwt-cors_part2/
    - Tutorial I've followed, which is linked from the previous
      post: https://www.toptal.com/java/rest-security-with-jwt-spring-security-and-java
- Spring Security in general
    - Architecture of the filter system: https://docs.spring.io/spring-security/reference/servlet/architecture.html
    - Baeldung series on Spring Security: https://www.baeldung.com/security-spring
        - `HttpSecurity` vs `WebSecurity`: https://www.baeldung.com/spring-security-httpsecurity-vs-websecurity
- Storing passwords
    - https://www.dontpanicblog.co.uk/2022/03/14/spring-security-delegating-password-encoder/
    - https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html

## `/front-end`

Angular app

## TODO

- Fix front-end styling and get rid of Angular Material.
- Read about content negotiation for the /play
  endpoint (https://spring.io/blog/2013/05/11/content-negotiation-using-spring-mvc)
- Add a unique id for songs in the playlist. Now when you remove one, all will be removed
- Custom playlists (requires users)
- Deploy! Containerise so it can be run with one command.
- Log everything
- Look into HATEOAS, maybe it allows to hide entity ids from URLs??

### TODO - Long term

- Replace search for ElasticSearch https://reflectoring.io/hibernate-search/
- Redis cache

### DONE

- Pagination for albums (also back-end)
- Specification for searching albums, inspired on https://attacomsian.com/blog/spring-data-jpa-specifications.
- For the test project: create a fake beets database for the back-end application to use. Have a few albums/songs in it.
