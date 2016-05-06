# {{name}}


## Development

Open a terminal and type `lein repl` to start a Clojure REPL
(interactive prompt).

In the REPL, type

```clojure
(run)
(browser-repl)
```

The call to `(run)` starts the Figwheel server at port 3449, which takes care of
live reloading ClojureScript code and CSS. Figwheel's server will also act as
your app server, so requests are correctly forwarded to the http-handler you
define.

Running `(browser-repl)` starts the Weasel REPL server, and drops you into a
ClojureScript REPL. Evaluating expressions here will only work once you've
loaded the page, so the browser can connect to Weasel.

When you see the line `Successfully compiled "resources/public/js/compiled/app.js" in 21.36
seconds.`, you're ready to go. Browse to `http://localhost:3449` and enjoy.

**Attention: It is not needed to run `lein figwheel` separately. Instead we
launch Figwheel directly from the REPL**

## Trying it out

If all is well you now have a browser window saying 'Hello Cubane!',
and a REPL prompt that looks like `cljs.user=>`.

Any change to the code will be automatically reloaded in the browser by figwheel.


Use
```clojure
({{name}}.figwheel/on-reload) ; to force the renderer to refresh the view and see the change
```
to force the rendering of the new application state.

### Clojurescript tests

To run the Clojurescript tests, do

```
lein doo phantom
```

## Settings

The following settings can be set as Environment Variables:
  - PORT the port to listen to, e.g. PORT=12345 ;
  - ORIENT_DB_STORE change the OrientDb store, e.g. ORIENT_DB_STORE=memory:tmp or ORIENT_DB_STORE=plocal:db ;
  - JWS-AUTH-SECRET to change the JWS token secret, e.g. JWS-AUTH-SECRET=my-secret ;
  - JWS-AUD to change the audience of the issued JWS tokens ;
  - JWS-ISS to change the issuer of the issued JWS tokens ;

## Production build

The production code can be tested using
```
lein with-profile +uberjar run
```

A full (standalone) jar can be created using
```
lein uberjar
```

On production, use the following command to launch your application:
```
PORT=12345 java -jar <jar name>.jar
```

## License

Copyright © 2016 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

## Cubane

Created with [Cubane](https://github.com/s-ted/cubane) {{cubane-version}}.
