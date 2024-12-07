# Test application

This is an application that only has tests and performs these
against a running instance of the back-end.

## How to run the tests

1. Open the file `test_music_library.db` under the "src/test/resources" folder in this project.
   Use some database administration tool like for example DBeaver.

2. Run the following SQL commands on the test database. You need to replace `<RESOURCES_PATH>` by
   the absolute path of this project's "src/test/resources" folder on your system.

```sql
UPDATE items
SET path = replace(path, '/Users/dev/Developer/music/tests/src/test/resources', '<RESOURCES_PATH>');
```

```sql
UPDATE albums
SET artpath = replace(path, '/Users/dev/Developer/music/tests/src/test/resources', '<RESOURCES_PATH>');
```

3. Before running the tests, the back-end application needs to be running. In order for the real application
   to use the test database, you need to set the `BEETS_DB_FILE` environment variable to point to the
   test database. With `<RESOURCES_PATH>` meaning the same as in step 2, you need to do the following:

```bash
export BEETS_DB_FILE=<RESOURCES_PATH>/test_music_library.db
```

4. Run the back-end application.
5. You can run now the tests application.

## Documentation

This is for my own future reference.

### Gherkin keywords

- **given()**: for configuring the request: content type, setting cookies, adding auth, adding parameters, setting
  headers...
- **when()**: for sending the GET/POST/PUT/DELETE request.
- **then()**: for validating the response: status code, content type, body, headers, cookies, ..

### Written tutorials checked

- https://www.baeldung.com/rest-assured-tutorial

### Video Tutorials seen

- https://www.youtube.com/watch?v=Orn8cP1yRJc
- https://www.youtube.com/watch?v=OM4mr3PKgcQ&list=PLUDwpEzHYYLvLZX_QEGTNolPvNADXid0I