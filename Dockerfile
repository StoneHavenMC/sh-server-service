FROM amazoncorretto:17.0.8-alpine3.18
COPY ./sh-server-service-infrastructure/target/application.jar app.jar
CMD ["java", "-jar", "app.jar"]