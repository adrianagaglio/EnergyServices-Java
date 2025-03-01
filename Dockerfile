# Stage di build: installa Maven su JDK 21
FROM eclipse-temurin:21 AS builder

# Installa Maven
RUN apt-get update && apt-get install -y maven

WORKDIR /app
COPY . .
RUN mvn clean install -DskipTests

# Stage finale: runtime
FROM eclipse-temurin:21
WORKDIR /app
COPY --from=builder /app/target/energyservices-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar", "energyservices-0.0.1-SNAPSHOT.jar"]