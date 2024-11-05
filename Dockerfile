# Importing JDK and copying required files
FROM openjdk:8u252 AS build
WORKDIR /app
COPY pom.xml .
COPY src src
COPY .env .
COPY install.sh .

# Copy Maven wrapper
COPY mvnw .
COPY .mvn .mvn

# Set execution permission for the Maven wrapper
RUN chmod +x ./mvnw
RUN ./mvnw clean package

# Export environment variables
RUN chmod +x ./install.sh
CMD ./install.sh

# Stage 2: Create the final Docker image using OpenJDK 19
FROM openjdk:8u252
VOLUME /tmp

# Copy the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=docker","/app.jar"]
EXPOSE 8080