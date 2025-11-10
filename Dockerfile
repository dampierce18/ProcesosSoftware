FROM amazoncorretto:17

WORKDIR /app
COPY target/sistema-minimarket-1.0.0.jar app.jar
RUN mkdir -p logs
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]