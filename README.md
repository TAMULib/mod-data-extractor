# mod-spring-sample

Copyright (C) 2018 The Open Library Foundation

This software is distributed under the terms of the Apache License, Version 2.0.
See the file ["LICENSE"](LICENSE) for more information.

## Additional information

Add Oracle OJDBC to local Maven repository:

`mvn install:install-file -Dfile="lib/ojdbc8.jar" -DgroupId="com.oracle" -DartifactId="ojdbc8" -Dversion="12.2.0.1" -Dpackaging="jar" -DgeneratePom=true`

## Docker deployment

```
docker build -t folio/mod-data-extractor .
docker run -d -p 9002:9002 folio/mod-data-extractor
```

### Publish docker image

```
docker build -t [docker repo]/folio/mod-data-extractor:[version] .
docker push [docker repo]/folio/mod-data-extractor:[version]
```

### Issue tracker

See project [FOLIO](https://issues.folio.org/browse/FOLIO)
at the [FOLIO issue tracker](https://dev.folio.org/guidelines/issue-tracker/).
