# Usa una imagen base de Java
FROM eclipse-temurin:17-jre-alpine

# Crea un directorio para la app
WORKDIR /app

# Copia el jar generado al contenedor
COPY target/inventario-service-*.jar app.jar

# Expone el puerto (ajusta si usas otro)
EXPOSE 8081

# Comando para ejecutar la app
ENTRYPOINT ["java", "-jar", "app.jar"]