# Microservices project

### Tech:
> - Spring-Boot 2.17
> - Eureka
> - Zuul Gateway
> - Actuator 
> - RabbitMQ
> - Feign
> - Zipkin
> - ELK

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

### Zipkin
- TraceID - unique one for the whole chain of requests and responses 
- SpanID - unique between two microservices

[Zipkin quickstart](https://zipkin.io/pages/quickstart.html)

```
docker run -d -p 9411:9411 openzipkin/zipkin
```
or
```
curl -sSL https://zipkin.io/quickstart.sh | bash -s
java -jar zipkin.jar
```

### ELK
- [ELK Products](https://www.elastic.co/)
  - [Logstash](https://www.elastic.co/downloads/logstash)
  - [Elasticsearch]()
 
##### Logstash 
```
docker pull docker.elastic.co/logstash/logstash:7.5.2
docker run --rm -it -v C:\tmp\logstash:/usr/share/logstash/config/ docker.elastic.co/logstash/logstash:7.5.2
```
or
```
.\bin\logstash.bat -f ..\logstash.conf
```
example logstash.conf
```shell script
input {
  file {
    type => "users-ms-log"
    path => "C:/Git/java/springTraining/microservices/users-ms.log"
  }
  
  file {
    type => "albums-ms-log"
    path=>"C:/Git/java/springTraining/microservices/albums-ms.log"
  }
}

output {
  if [type] == "users-ms-log" {
      elasticsearch {
        hosts => ["localhost:9200"]
        index => "users-ms-%{+YYYY.MM.dd}"
      }
  }
  if [type] == "albums-ms-log" {
    elasticsearch {
      hosts => ["localhost:9200"]
      index => "albums-ms-%{+YYYY.MM.dd}"
    }
  }
  stdout { codec => rubydebug }
}
```

##### Elasticsearch
```
docker pull docker.elastic.co/elasticsearch/elasticsearch:7.5.2
docker run -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:7.5.2
```
```
http://127.0.0.1:9200/
http://127.0.0.1:9200/_cat - additional commands
http://127.0.0.1:9200/users-ms-2020.02.04/_search?q=*
http://127.0.0.1:9200/users-ms-2020.02.04/_search?q=*&format&pretty
http://127.0.0.1:9200/users-ms-2020.02.04/_search?q=message:users&format&pretty
```

##### Kibana
```
docker pull docker.elastic.co/kibana/kibana:7.5.2
docker run -p 5601:5601 -e "ELASTICSEARCH_HOSTS=http://localhost:9200" docker.elastic.co/kibana/kibana:7.5.2
```
