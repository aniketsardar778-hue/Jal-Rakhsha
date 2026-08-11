FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

COPY mvnw .

COPY pom.xml .

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

COPY src src

RUN ./mvnw clean package -DskipTests


FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8085


CMD ["java", "-jar", "target/jalrakhsa-0.0.1-SNAPSHOT.jar"]
