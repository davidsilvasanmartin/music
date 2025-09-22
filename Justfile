set dotenv-load := true

# TODO: change this variable to where your beets library is stored

beets_db_file_rel_path := "../../music/music_library.db"
uv := "uv"
docker := "docker"
tests_beets_db_file_rel_path := "./tests/src/test/resources/test_music_library.db"

# Lists available commands. Gets executed when running `just` with no args
default:
    @just --list --unsorted

# Runs clean install in all backend projects
binstall:
    mvn -DskipTests -f ./tests/pom.xml clean install
    mvn -DskipTests -f ./back-end-schema-update/pom.xml clean install
    mvn -DskipTests -f ./back-end/pom.xml clean install

# Starts the app database if not already started
bdbstart:
    if [ -z "$({{ docker }} compose ps --status=running -q database)" ]; then \
      {{ docker }} compose up -d database; \
      sleep 10; \
    fi

# Runs the backend schema update
bschema: bdbstart
    #!/bin/sh
    abs_file=`./scripts/get_abs_path.sh {{ beets_db_file_rel_path }}`
    BEETS_DB_FILE="$abs_file" mvn -f ./back-end-schema-update/pom.xml spring-boot:run

# Starts the backend
bstart: bdbstart
    #!/bin/sh
    abs_file="$(./scripts/get_abs_path.sh {{ beets_db_file_rel_path }})"
    BEETS_DB_FILE="$abs_file" mvn -f ./back-end/pom.xml spring-boot:run

# [TESTING MODE] Starts the app database if not already started
bdbstart_test:
    if [ -z "$({{ docker }} compose ps --status=running -q database-test)" ]; then \
      {{ docker }} compose up -d database-test; \
      sleep 10; \
    fi

# [TESTING MODE] Runs the backend schema update
bschema_test: bdbstart_test
    #!/bin/sh
    abs_file=`./scripts/get_abs_path.sh {{ tests_beets_db_file_rel_path }}`
    BEETS_DB_FILE="$abs_file" mvn -f ./back-end-schema-update/pom.xml spring-boot:run


# [TESTING MODE] Starts the backend
bstart_test: bdbstart_test
    #!/bin/sh
    abs_file=`./scripts/get_abs_path.sh {{ tests_beets_db_file_rel_path }}`
    BEETS_DB_FILE="$abs_file" mvn -f ./back-end/pom.xml spring-boot:run

# [TESTING MODE] Runs API integration tests. Needs the backend to be running in testing mode
btest:
    mvn -f ./tests/pom.xml test

# Cleans front-end cache and builds and reinstalls the libraries
finstall:
    cd front-end && npm ci

# Starts the frontend
fstart:
    cd front-end && npm start
