FROM adoptopenjdk/openjdk11:alpine-jre
COPY target/gateway-1.0-SNAPSHOT.jar gateway-1.0-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/gateway-1.0-SNAPSHOT.jar"]