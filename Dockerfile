FROM maven:3.9.2-eclipse-temurin-17-alpine AS MAVEN_BUILD

WORKDIR /app

COPY pom.xml .

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=MAVEN_BUILD /app/target/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

# commands
# docker network create baloot-network
# docker run --name mysql-container --network baloot-network -e MYSQL_ROOT_PASSWORD=root_1234 -e MYSQL_DATABASE=balootdb -d mysql:8.0.33
# docker build -t back .
# docker run --network baloot-network --name baloot-container -p 8080:8080 -d back



