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