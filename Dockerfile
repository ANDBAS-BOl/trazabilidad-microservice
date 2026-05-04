# JDK 17 y Gradle 8.5 alineados con build.gradle y gradle-wrapper.properties.
# Se usa el binario `gradle` de la imagen oficial para no depender de descargar el ZIP del wrapper en el build de Docker.
FROM gradle:8.5-jdk17-jammy AS build
WORKDIR /app

COPY gradlew settings.gradle build.gradle ./
COPY gradle gradle
COPY src src

RUN sed -i 's/\r$//' ./gradlew \
    && gradle bootJar -x test --no-daemon \
    && mv build/libs/trazabilidad-microservice-*.jar /app/application.jar

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

ARG APP_VERSION=0.0.1-SNAPSHOT
LABEL org.opencontainers.image.title="trazabilidad-microservice" \
      org.opencontainers.image.version="${APP_VERSION}"

COPY --from=build /app/application.jar app.jar

EXPOSE 8083
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
