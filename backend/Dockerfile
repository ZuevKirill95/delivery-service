FROM maven:3-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17.0.8.1_1-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar /app/*.jar
EXPOSE 8081
CMD [ "java", "-jar", "/app/*.jar" ]