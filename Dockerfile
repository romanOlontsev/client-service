FROM openjdk:17-slim
COPY target/client-service-0.0.1-SNAPSHOT.jar /client-service.jar
ENTRYPOINT exec java -jar /client-service.jar