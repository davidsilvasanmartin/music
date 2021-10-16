# Music

This project is a Plex-like website that works with Beets

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
