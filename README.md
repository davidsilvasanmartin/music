# Music

This project is a Plex-like website that works with Beets

## Music

Folder that contains a Beets library. This would be somewhere else in our filesystem.

## Back-end

Spring application running on port 8080.

## Front-end

Angular app

## TODO

- Fix front-end styling and get rid of Angular Material.
- Read about content negotiation for the /play
  endpoint (https://spring.io/blog/2013/05/11/content-negotiation-using-spring-mvc)
- Add a unique id for songs in the playlist. Now when you remove one, all will be removed
- Custom playlists (requires users)
- Deploy!
- Log everything

### TODO - Long term

- Replace search for ElasticSearch https://reflectoring.io/hibernate-search/
- Redis cache

### DONE

- Pagination for albums (also back-end)
- Specification for searching albums, inspired on https://attacomsian.com/blog/spring-data-jpa-specifications.
- Peek into an albumDto: see songs one by one
