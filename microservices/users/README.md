# Microservices project

### Tech:
> - Spring-Boot 2.17
> - Eureka
> - Zuul Gateway
> - Actuator 
> - RabbitMQ

### Useful commands
```
mvn spring-boot:run -Dspring-boot.run.arguments=--spring.application.instance_id=meme
mvn spring-boot:run -Dspring-boot.run.arguments=--spring.application.instance_id=meme,--server.port=9999
```
spread changes in application.properties to all micro
```
POST: localhost:8012/actuator/bus-refresh
```
### Properties for config server
```
application.properties - local file, lowest priority 
application.properties - config server, for all microservices
<microservice-name>.properties - config server, for microservices with spring.application.name=<microservice-name>
```
```
GET: localhost:8012/ConfigServer/default
GET: localhost:8012/users-ms/default
```