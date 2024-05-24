# Utilizar una imagen base de OpenJDK con Java 17
FROM openjdk:17-jdk-slim

# Añadir el jar de la aplicación al contenedor
COPY target/microservicio-cuenta-movimientos-0.0.1-SNAPSHOT.jar /app/microservicio-cuenta-movimientos-0.0.1-SNAPSHOT.jar

# Exponer el puerto que utiliza la aplicación
EXPOSE 8081

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/microservicio-cuenta-movimientos-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=docker"]
