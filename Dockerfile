FROM maven:3.8.5-openjdk-21
WORKDIR /app
COPY . .
RUN mvn clean install
CMD ["java", "-jar", "target/nome-app.jar"]
