FROM maven:3.9.2-eclipse-temurin-21 AS builder
WORKDIR /app
COPY . .
RUN mvn clean install

FROM eclipse-temurin:21
WORKDIR /app
COPY --from=builder /app/target/nome-app.jar .
CMD ["java", "-jar", "energyservices-0.0.1-SNAPSHOT.jar"]