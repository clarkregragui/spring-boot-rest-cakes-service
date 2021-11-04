Cakes Service Micro Service (fictitious)
=======================================


Solution delivered as Spring Boot Rest Service Application secured with Github OAuth2
and integration tests against fully deployed application.

Github Actions script used for CI, Docker Image building and CD of image to DockerHub

GitHub

https://github.com/clarkregragui/spring-boot-rest-cakes-service

DockerHub

https://hub.docker.com/repository/docker/clarkregragui/docker-spring-rest-cakes



TECHNOLOGIES

Maven
Spring Boot
Spring Data JPA
Spring Security 5 (OAuth2 for Github)
Spring OpenApi (Swagger 3)
Spring Freemarker
H2DB
Spring Test
Junit5
Mockito
Docker



To run

mvn package

followed by

mvn spring-boot:run

To run Docker image

docker run -d -p 8080:8080 clarkregragui/docker-spring-rest-cakes:latest

Alternatively directly with

java -jar rest-cakes-service-1.0-SNAPSHOT.jar

Under my java 11 installation I required -Djdk.tls.client.protocols=TLSv1.2 in order for PSK handshake to succceed for multiuser login
as TLSv1.3 PSK(pre-shared-key) handshake was failing for some reason. So depending on your Java installation, you may need 

-Djdk.tls.client.protocols=TLSv1.2 

if you have any problem logging in using Github OAuth2.


Additionally for the OAuth2 version, run with profile prod:

-Dspring.profiles.active=prod

Without OAuth2:

-Dspring.profiles.active=test

URLs

Swagger API Docs

http://localhost:8080/v3/api-doc

http://localhost:8080/api-docs.yaml

http://localhost:8080/swagger-ui.html


Velocity template HTML Cakes View

GET http://localhost:8080/

Pretty Print Json Cakes List

GET http://localhost:8080/cakes

Create new Cake with CakeDTO Json HttpRequest.Body

POST http://localhost:8080/cakes













