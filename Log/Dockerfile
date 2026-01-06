FROM maven:3.9.12-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine AS prod
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar
CMD ["app.jar"]
EXPOSE 8080