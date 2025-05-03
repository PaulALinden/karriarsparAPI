FROM eclipse-temurin:24

LABEL authors="linde"

WORKDIR /app

COPY build/libs/app.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=${PORT:-8080}"]