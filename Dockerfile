FROM maven:3.8.3-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src/ /app/src
RUN mvn clean install -DskipTests

FROM openjdk:17-alpine
EXPOSE 8080
COPY --from=build /app/target/techchallenge-cliente.jar techchallenge-cliente.jar
ENTRYPOINT ["java","-jar","techchallenge-cliente.jar"]