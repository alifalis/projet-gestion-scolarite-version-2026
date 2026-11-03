# Étape 1 : compilation
FROM eclipse-temurin:25-jdk AS build

WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

RUN chmod +x mvnw

COPY src ./src

RUN ./mvnw clean package -DskipTests

# Étape 2 : exécution
FROM eclipse-temurin:25-jre

WORKDIR /app

COPY --from=build /app/target/*.war app.war

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.war"]
