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

### TODO - Long term

- Replace search for ElasticSearch https://reflectoring.io/hibernate-search/
- Redis cache

### DONE

- Pagination for albums (also back-end)
- Specification for searching albums, inspired on https://attacomsian.com/blog/spring-data-jpa-specifications.
- For the test project: create a fake beets database for the back-end application to use. Have a few albums/songs in it.
