FROM maven:3.9.8-sapmachine-22 as builder
WORKDIR /opt/app
COPY mvnw pom.xml ./
COPY ./src ./src
RUN mvn clean install -DskipTests

FROM openjdk:22
WORKDIR /opt/app
COPY --from=builder opt/app/target/*.jar opt/app/*.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "--enable-preview", "opt/app/*.jar"]