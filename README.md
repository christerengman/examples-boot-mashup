# examples-boot-mashup

[![Build Status](https://travis-ci.org/christerengman/examples-boot-mashup.svg?branch=master)](https://travis-ci.org/christerengman/examples-boot-mashup)

## Description

Mashup REST API using Spring Boot.

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

The application is also packaged as an standalone jar which can be run as follows:
```
$ java -jar target/boot-mashup-0.0.1-SNAPSHOT.jar

``` 


## Design

The application uses [Spring Boot](http://projects.spring.io/spring-boot/) application framework and was initially setup using the [Spring Initializr](https://start.spring.io/) to get a solid and production-ready application up and running in shortest time possible.

The application also aligns with Spring's architecture with a service layer for abstracting the external information sources as illustrated in the diagram:

![Component Diagram](http://plantuml.com/plantuml/png/NOun2iCm34Ltdy9upo7Ip9qwT0WT54LGKTSEikj2Jryx2iLq47p-WwSFcAFOoJE5R25PDFCecLEUOiWIlIUvjHugEByIuqc0m-T2KXXfHyybcVdA3zvePUZ9UwjaYeMaEaV1EnVgEK8oAsHO895rdTwfjuC_lrDnX_uUE3KROz0EGniEUsWpwsaykZQmdiQZQwFgQVm2)

The components are implemented quite differently in regards to JSON processing which is described here:

### ArtistController

This controller is the entry endpoint for the application and represents the API exposed to clients. It calls all services in turn to aggregate the information from them. For JSON serializing, it uses Jackson's data binding method together with a typical domain model consisting of the `Artist` and it's `Album`s. This is by far the most clear and maintainable way of declaring structured JSON.


### MusicBrainzService

Fetches artist and album information through the MusicBrainz API. Similar to the `ArtistController`, it uses Jackson data binding, but this time for JSON parsing. The reason for this decision is that the service returns a multi-level hierarchical structure which would have been hard to parse and traverse using another model.


### WikipediaService

Searches for artist description in Wikipedia through their API. As only a single field is read it uses the Jackson tree model method for extracting the information.  
 

### CoverArtArchiveService

Finds cover art URLs for each album returned by the `MusicBrainzService`. JSON parsing is done using Jayway's JSONPath as it is perfect for this case where only images belonging to front covers should be returned (filtering). 


## Test Driven (Integration) Development

I love TDD and my favorite test framework for integration solutions like this is [Citrus Framework](http://www.citrusframework.org/) which I setup and used for creating an initial test case describing the requirements. To get the test green I created a Spring MVC Controller with REST annotations and with hard-coded values as response. From there each new feature was implemented by replacing the hard-coded values in a top-down manner.

In Citrus, I defined all external services as separate servers to distinguish between them in the test case. To be able to switch between these simulation servers (at http://localhost:xxxx) and the real ones, I had to introduce the Spring profile `integration-test` which automatically is activated upon running the application in the `integration-test` Maven phase. The different baseURIs are stored in the `application.yml` file and are applied to each service using annotations.


## Known Issues

The external services are slow and rate limited which occasionally causes HTTP 503 errors all the way back to the client. Caching has been implemented but of course only helps for repetitive calls for the same artist.