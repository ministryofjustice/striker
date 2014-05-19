# striker

A Clojure library designed to cross out lines of text in a PDF
document.

## Usage

Make sure you have Java & [Leiningen](http://leiningen.org/) installed. e.g. on Mac OSX:

    $ brew install leiningen

Then:

    $ lein uberjar
    $ java -jar target/striker-0.3.1-standalone.jar \
        -i form.pdf \
        -o work.pdf \
        -j strikes.json

Where **strikes.json** is in the format of:

    {
        "strikes": [
            { "page": 2, "x": 267, "y": 557, "x1": 40, "y1": 0, "thickness": 2 },
            { "page": 2, "x": 309, "y": 557, "x1": 37, "y1": 0, "thickness": 2 }
        ],
        "flatten": false
    }


## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
