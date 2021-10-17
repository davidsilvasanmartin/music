# Music

This project is a Plex-like website that works with Beets

## Music

Folder that contains a Beets library. This would be somewhere else in our filesystem.

## Back-end

### Architecture

Using microservices

- Music service: uses Beets db.
- Users service: uses a postgres db.

### Port assignment

- Api gateway: 8000
- Discovery server: 8100
- Music service: 8200
- Users service: 8300

## Front-end

Angular app

## TODO

- Add a unique id for songs in the playlist. Now when you remove one, all will be removed
- Custom playlists (requires users service)
- Deploy!
- Log everything

### TODO - Long term

- Replace search for ElasticSearch https://reflectoring.io/hibernate-search/
- Redis cache

### DONE

- Pagination for albums (also back-end)
- Peek into an album: see songs one by one
