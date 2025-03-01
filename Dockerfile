FROM maven:3.8.5-openjdk-21
WORKDIR /app
COPY . .
RUN mvn clean install
CMD ["java", "-jar", "target/energyservices-0.0.1-SNAPSHOT.jar"]
