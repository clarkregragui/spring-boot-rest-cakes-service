FROM adoptopenjdk/openjdk11:alpine-jre

EXPOSE 8080

ARG JAR_FILE=target/rest-cakes-service-1.0-SNAPSHOT.jar

WORKDIR /opt/app

COPY ${JAR_FILE} rest-cakes-service.jar

ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","rest-cakes-service.jar"]
