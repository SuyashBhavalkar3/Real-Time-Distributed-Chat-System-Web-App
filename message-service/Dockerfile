# ---------- Build Stage ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build

# build shared schema first
COPY chat-event-schema chat-event-schema
RUN mvn -f chat-event-schema/pom.xml clean install -DskipTests

# build message service
COPY message-service message-service
RUN mvn -f message-service/pom.xml clean package -DskipTests

# ---------- Runtime Stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /build/message-service/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]