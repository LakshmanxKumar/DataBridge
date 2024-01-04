FROM eclipse-temurin:17.0.8.1_1-jre-alpine
WORKDIR /app
COPY /serviceAccountKey.json serviceAccountKey.json
COPY target/DataBridge-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]
EXPOSE 8080