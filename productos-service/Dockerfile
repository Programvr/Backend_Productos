# Usa una imagen de Java 21
FROM eclipse-temurin:21-jre-alpine

# Crea un directorio para la app
WORKDIR /app

# Copia el jar generado por Maven
COPY target/productos-service-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto de la app
EXPOSE 8080

# Comando para ejecutar la app
ENTRYPOINT ["java", "-jar", "app.jar"]