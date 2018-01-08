JSON processor
================

Simple Spring Boot application for processing dummy JSONs with two REST endpoint:
 1. takes any valid JSON, send it to Redis, a listener calls a service with the freshly received message which is persists it to a PostgreSQL database and pushes it through Websocket. 
 2. retrieving  all persisted messages from database
 
## Running
The easiest way to run it locally is using docker-compose.
All you have to do is run `docker-compose up` (of course you have to run Docker on your machine).

When everything is up and running you can find a Swagger UI which can help you to try out the REST endpoints: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

You can find a simple page at [http://localhost:8080](http://localhost:8080) and see the incoming messages from Websocket.

## Testing
There are unit tests which can running by `mvn test`, and there is an integration test which is separated from unit tests, you can run it by `mvn integration-test -P integration` (actually this runs unit and integration tests as well).
For passed integration test you have to run Redis on your machine (e.g. `docker run --name redis -p 6379:6379 redis`).

## Developing
For reducing boilerplate code, this project using Lombok. If you would like to import the project into your IDE, you have to download and turn on Lombok plugin.
You can find more information about Project Lombok [here](https://projectlombok.org/).
