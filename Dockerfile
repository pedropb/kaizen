FROM maven:3.6-jdk-8-slim as build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean test verify package

FROM openjdk:8
COPY --from=build /usr/src/app/target/users-1.0.jar /usr/src/app/
WORKDIR /usr/src/app
ENTRYPOINT ["java", "-jar", "users-1.0.jar"]
