https://beets.readthedocs.io/en/stable/reference/config.html

## How to run beets to create a music library

1. Open `config.yaml`. Change the following variables:
    - `directory`: this is the directory where the actual music collection will be stored.
This directory needs to  be specified in the back-end project too.
    - `library`: this is the sqlite database file that beets will create and use for
keeping track of all the music.

2. (Optional but highly recommended). Create a Python virtual environment and activate it:
```shell
python3 -m venv ./.venv
source ./.venv/bin/activate
```

3. Install the required libraries

```shell
pip install -r requirements.txt
```

- Please note, the libraries that were installed originally are as follows:
`pip install beets[fetchart,lyrics,lastgenre,scrub] flask`

4. Copy music you want to import into beets into the `export` directory,
under this same folder.
- Tip: each folder under `export/` should be one album. Don't create artist folders
that include several albums each.
 
5. Finally, use beets to import the music into your library.

```shell
beet -c $(pwd)/config.yaml import $(pwd)/export/
```
