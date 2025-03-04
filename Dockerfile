# Usa un'immagine base con Java 21
FROM eclipse-temurin:21-jdk-jammy


# Imposta la directory di lavoro all'interno del container
WORKDIR /app


# Copia il file JAR dell'applicazione nel container
COPY target/*.jar app.jar


# Esponi la porta su cui l'applicazione Spring Boot è in ascolto
EXPOSE 8080


# Comando per eseguire l'applicazione
ENTRYPOINT ["java", "-jar", "app.jar"]
