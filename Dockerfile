
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/payment-app-1.0-SNAPSHOT.jar app.jar

CMD ["java", "-cp", "app.jar", "com.example.Payment"]
