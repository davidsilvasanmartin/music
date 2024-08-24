# Music

This project is a Plex-like website that works with Beets

## Music

Folder that contains a Beets library. This would be somewhere else in our filesystem.

## Back-end

### Architecture

Spring application running on port 8080.

## Front-end

Angular app

## TODO

- Fix front-end styling and get rid of Angular Material.
- Add a unique id for songs in the playlist. Now when you remove one, all will be removed
- Custom playlists (requires users)
- Deploy!
- Log everything

### TODO - Long term

- Replace search for ElasticSearch https://reflectoring.io/hibernate-search/
- Redis cache

### DONE

- Pagination for albums (also back-end)
- Peek into an album: see songs one by one
