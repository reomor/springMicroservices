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
run rabbitMQ plugin
```
rabbitmq-plugins enable rabbitmq_management
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
/diskC-tmp/application.properties <br>
/diskC-tmp/users-ms.properties <br>

### Actuator
[Spring-Boot actuator endpoints]([https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-endpoints.html) <br>

### Java Cryptography Extension (JCE)
[Download JCE](https://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html)
put *.jar into C:\Program Files\Java\jdk1.8.0_172\jre\lib\security <br>
#### Symmetric
add to config bootstrap.properties 
```properties
encrypt.key=kghop45y9jhh&*y87Gubib&*GUhoijgportjgriohjrth
```
encrypt/decrypt parameter
```
POST: http://localhost:8012/encrypt
POST: http://localhost:8012/decrypt
```
#### Asymmetric
generate keystore
```
keytool -genkeypair -alias apiEncryptionKey -keyalg RSA \
-dname "CN=User Userof,OU=API Development,O=mydev.com,L=Moscow,S=ON,C=CA" \
-keypass 1q2w3e4r -keystore apiEncryptionKey.jks -storepass 1q2w3e4r
keytool -importkeystore -srckeystore apiEncryptionKey.jks -destkeystore apiEncryptionKey.jks -deststoretype pkcs12
```
the same in one string
```
keytool -genkeypair -alias apiEncryptionKey -keyalg RSA -dname "CN=User Userof,OU=API Development,O=mydev.com,L=Moscow,S=ON,C=CA" -keypass 1q2w3e4r -keystore apiEncryptionKey.jks -storepass 1q2w3e4r
keytool -importkeystore -srckeystore apiEncryptionKey.jks -destkeystore apiEncryptionKey.jks -deststoretype pkcs12
```
```
POST: http://localhost:8012/encrypt
POST: http://localhost:8012/decrypt
```