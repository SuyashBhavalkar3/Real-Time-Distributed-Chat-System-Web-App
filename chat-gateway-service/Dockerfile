# ---------- Build Stage ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build

# build shared schema first
COPY chat-event-schema chat-event-schema
RUN mvn -f chat-event-schema/pom.xml clean install -DskipTests

# build gateway service
COPY chat-gateway-service chat-gateway-service
RUN mvn -f chat-gateway-service/pom.xml clean package -DskipTests

# ---------- Runtime Stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /build/chat-gateway-service/target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]