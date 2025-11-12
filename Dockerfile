FROM eclipse-temurin:17-jdk
WORKDIR /app
ARG JAR_FILE=target/skillup-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
