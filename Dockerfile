
#Development
FROM eclipse-temurin:25-jdk AS dev
WORKDIR /app

#Compilation
FROM dev AS build
COPY . .
RUN ./mvnw package -DskipTests

#Production
FROM eclipse-temurin:25-jre AS prod
WORKDIR /app
COPY --from=build /app/target/community-*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
