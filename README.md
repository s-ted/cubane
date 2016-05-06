# cubane

  [![Dependencies Status](http://jarkeeper.com/cubane/cubane/status.svg)](http://jarkeeper.com/cubane/cubane)
  [![Download Status](https://jarkeeper.com/cubane/cubane/downloads.svg)](https://jarkeeper.com/cubane/cubane)

A Leiningen template for generating a full website project in Clojure + ClojureScript using the [cubanostack](https://github.com/s-ted/cubanostack) stack:
  - [cublono-quiescent](https://github.com/s-ted/cublono-quiescent) for easy writing of ReactJS component in,
  - [bidi](https://github.com/juxt/bidi) for routing both in Clojure and ClojureScript,
  - [postal](https://github.com/drewr/postal) for sending mail,
  - [liberator](https://github.com/clojure-liberator/liberator) for RESTfull backend coding,
  - [buddy](https://github.com/funcool/buddy) for user authentication (using JWT),
  - [hiccup](https://github.com/weavejester/hiccup) for HTML generation,
  - [slingshot](https://github.com/scgilardi/slingshot) for advanced exception handling,
  - [timbre](https://github.com/ptaoussanis/timbre) for logging,
  - [tower](https://github.com/ptaoussanis/tower) for i18n,
  - (optional) [sente](https://github.com/ptaoussanis/sente) for WebSocket communication between frontend and backend,
  - [component](https://github.com/stuartsierra/component) for module definition,
  - [schema](https://github.com/plumatic/schema) for data integrity and coercion,
  - [ring](https://github.com/ring-clojure/ring) for the web application abstraction,
  - [http-kit](https://github.com/http-kit/http-kit) for serving the web application,
  - (optional) [orientdb](https://github.com/orientechnologies/orientdb) offering an embedded Java document noSql database,
  - [clj-http](https://github.com/dakrone/clj-http) for inter-server communication,
  - [cljs-http](https://github.com/r0man/cljs-http) to communicate with the RESTFull backend services.

## Usage

```
lein new cubane <your project name> --snapshot -- --http-kit --orient-db
```

## License

Copyright © 2016 [Sylvain Tedoldi](https://github.com/s-ted)

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
