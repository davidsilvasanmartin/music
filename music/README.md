https://beets.readthedocs.io/en/stable/reference/config.html

Install (after creating venv: `py -m venv ./.venv`):

```
pip install beets[fetchart,lyrics,lastgenre,scrub] discogs-client flask
```

Or, alternatively, install requirements.txt.

Set env var for config EVERYTIME A NEW CONSOLE IS OPENED. AND ACTIVATE VENV

UPDATE: setting the env variable was only needed to apply config file, now I'm passing it via -c option (see section on running beets below).

```
.\.venv\Scripts\activate
set BEETSDIR=.
```

Import all

```
beet import ./export
```

- Lyrics error fix: https://github.com/beetbox/beets/issues/2805

- Put single tracks/albums in SEPARATE FOLDERS

## Running beets
Please modify the directories and config file as required.
```bash
 beet -c ~/dev/music/music/config.yaml import /media/dev/Data/export/
```
