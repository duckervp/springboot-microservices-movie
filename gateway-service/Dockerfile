FROM adoptopenjdk/openjdk11:alpine-jre
COPY target/gateway-service-0.0.1-SNAPSHOT.jar gateway-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/gateway-service-0.0.1-SNAPSHOT.jar"]