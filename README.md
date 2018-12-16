# Outfit Service

This service provides outfit recommendation depending on the current temperature at a given location.


## Weather data

Current weather data is retrieved using the Weather API from [OpenWeatherMap].


## DMN Decision Table

A DMN decision table is used to determine which outfit level corresponds to the temperature:

![Decision]

You can find the corresponding DMN XML file [decision.dmn11.xml] in the
resources. To modify it you can use the [Camunda Modeler].


## Running the Example

You can create a executable Java jar file with:

```
mvn clean package
```

This will produce a `outfit-service-0.0.1-SNAPSHOT.jar` file in the `target` folder. You can
than call it like any other jar file:

```
java -jar target/outfit-service-0.0.1-SNAPSHOT.jar 
```

## Tests

### Unit tests

The decision table is tested with JUnit using the Parametrized Runner to check many cases.

The Weather API client is tested with JUnit using the MockitoJUnitRunner to mock the RestTemplate.

The RestControllers are tested with JUnit using the MockitoJUnitRunner to mock business logic and services.

### Integration tests

Caching for the Weather API ist tested with JUnit using the SpringJUnit4ClassRunner in combination with Mockito.


## Endpoint documentation

You can visualize and interact with the API’s resources using [Swagger UI].


## Reports

Repots are available after running:
```
mvn site
```

### Unit test report

Unit test results can be viewed as a [Surefire report].

### Code coverage report

Code coverage for tests can be viewed as a [JaCoCo report].



[OpenWeatherMap]: https://www.openweathermap.org/api
[Camunda Modeler]: https://camunda.org/dmn/tool/
[Decision]: src/main/resources/decision.png
[decision.dmn11.xml]: src/main/resources/decision.dmn11.xml
[SpotBugs report]: target/site/spotbugs.html
[JaCoCo report]: target/site/jacoco/index.html
[Surefire report]: target/site/surefire-report.html
[Swagger UI]: http://localhost:8080/swagger-ui.html
