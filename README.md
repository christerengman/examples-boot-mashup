# examples-boot-mashup

## Description
Mashup REST API using Spring Boot

Aggregates information from the following three sources into a new REST API:

* MusicBrainz (http://musicbrainz.org/ws/2)
* Wikipedia (https://en.wikipedia.org/w/api.php)
* Cover Art Archive (http://coverartarchive.org)


## Build and Run

```
$ mvn install
$ mvn spring-boot:run
$ curl http://localhost:8080/artist/5b11f4ce-a62d-471e-81fc-a69a8278c7da
...
```
