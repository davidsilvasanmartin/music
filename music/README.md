https://beets.readthedocs.io/en/stable/reference/config.html

Install (after creating venv: `py -m venv ./.venv`):

```
pip install beets[fetchart,lyrics,lastgenre,scrub] discogs-client flask
```

Or, alternatively, install requirements.txt.

Set env var for config EVERYTIME A NEW CONSOLE IS OPENED. An ACTIVATE VENV

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
