# Usar imagen oficial de Maven con Java 17
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Imagen ligera para ejecución
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY --from=build /app/target/1aMarcha-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]