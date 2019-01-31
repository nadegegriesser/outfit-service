# Outfit Service

This service provides outfit recommendation depending on the current temperature at a given location (either by city id or by coordinates).


## Weather data

Current weather data is retrieved using the Weather API from [OpenWeatherMap].


## DMN Decision Table

A DMN decision table is used to determine which outfit level corresponds to the temperature:

![Decision]

You can find the corresponding DMN XML file [decision.dmn] in the
resources. To modify it you can use the [Camunda Modeler].


## Running the Example

The code hast been developed and tested with java 1.8.0_181 and Apache Maven 3.3.3.

You can create a executable Java jar file with:

```
mvn clean package
```

This will produce a `outfit-service-0.0.1-SNAPSHOT.jar` file in the `target` folder. You can
than call it like any other jar file:

```
java -jar target/outfit-service-0.0.1-SNAPSHOT.jar 
```

## Frontend

A minimal [Demo] frontend is available to demonstrate the use of the 2 endpoints.

If the browser supports geolocation and the user accepts to share its coordinates, the endpoint using coordinates is called.
![Coordinates]

Otherwise the user can select a country, a city in the previously selected country and query the endpoint using the city id. 
![City]

## Tests

### Unit tests

The decision logic is tested with JUnit using the Parametrized runner to check many cases. 
Error cases are tested with the standard JUnit runner using different DMN files.

The Weather API client is tested with JUnit using MockitoJUnitRunner to mock the RestTemplate.
Caching for the Weather API ist tested with JUnit using SpringJUnit4ClassRunner in combination with Mockito.

The RestControllers are tested with JUnit using MockitoJUnitRunner to mock business logic and services.


### Integration tests

The RestControllers are tested with JUnit using SpringRunner to start the web application, check request validation and test the responses.



## Endpoint documentation

You can visualize and interact with the API’s resources using [Swagger UI].


## Reports

Reports are available after running:
```
mvn site
```

### Unit test report

Unit test results can be viewed as a [Surefire report].

### Integration test report

Integration test results can be viewed as a [Failsafe report].

### Code coverage report

Code coverage for tests can be viewed as a [JaCoCo report].


## Possible improvements

- Download and extract the json file containing the cities when needed instead of saving it to the resources folder (determine if there is a newer version available using the ETag header).
- Write a maven plugin using JCodeModel to generate the Outfit Decider contract (Decision and Variables classes) from the DMN file.
- Define more complex decision logic, maybe based on humidity or pressure. 


[OpenWeatherMap]: https://www.openweathermap.org/api
[Camunda Modeler]: https://camunda.org/dmn/tool/
[Decision]: src/main/resources/decision.png
[decision.dmn]: src/main/resources/decision.dmn
[SpotBugs report]: target/site/spotbugs.html
[JaCoCo report]: target/site/jacoco/index.html
[Surefire report]: target/site/surefire-report.html
[Failsafe report]: target/site/failsafe-report.html
[Demo]: http://localhost:8080
[Coordinates]: src/main/resources/coordinates.png
[City]: src/main/resources/city_id.png
[Swagger UI]: http://localhost:8080/swagger-ui.html
