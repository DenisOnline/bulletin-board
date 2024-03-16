FROM maven-4.0.0-openjdk-19 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:19.0.2-jdk-slim
COPY --from=build /target/bulletin_board-0.0.1-SNAPSHOT.jar bulletin_board.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "bulletin_board.jar"]