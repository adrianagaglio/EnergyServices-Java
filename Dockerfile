# FASE DI BUILD: Usa Maven per compilare l'applicazione
FROM maven:3.8-eclipse-temurin-21 AS build
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests


# Usa un'immagine base con Java 21
FROM eclipse-temurin:21-jdk-jammy

# Imposta la directory di lavoro all'interno del container
WORKDIR /app


# Copia il file JAR dell'applicazione nel container
COPY --from=build /build/target/*.jar app.jar

# Crea le directory necessarie per le risorse
RUN mkdir -p /app/resources/templates
# Crea anche la directory che l'applicazione sta cercando specificamente
RUN mkdir -p /src/main/resources/templates

# Copia i file HTML dei template in entrambe le posizioni per sicurezza
COPY src/main/resources/templates/*.html /app/resources/templates/
COPY src/main/resources/templates/*.html /src/main/resources/templates/

# Esponi la porta su cui l'applicazione Spring Boot è in ascolto
EXPOSE 8080

# Comando per eseguire l'applicazione
ENTRYPOINT ["java", "-jar", "app.jar"]
