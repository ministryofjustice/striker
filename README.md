# striker

A Clojure library designed to cross out lines of text in a PDF
document.

## Usage

Make sure you have Java & Leiningen installed. Then:

    $ lein uberjar
    $ java -jar target/striker-0.1.0-standalone.jar -i form.pdf \
        -o work.pdf \
        -p 2 \
        --x 267 \
        --y 557 \
        --x1 40 \
        --y1 0 \
        -t 2

## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
