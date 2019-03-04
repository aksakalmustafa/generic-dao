# Step : Test and package
FROM maven:3.6-jdk-8-alpine as target
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src/ src/
RUN mvn package

# Step : Package image
FROM openjdk:8-jdk-alpine
VOLUME /app
EXPOSE 8080
COPY --from=target /build/target/*.jar /app/app.jar
CMD ["java", "-jar", "/app/app.jar"]