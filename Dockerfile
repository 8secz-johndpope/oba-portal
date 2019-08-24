# Start with a base image containing Java runtime
FROM openjdk:8-jdk-alpine as oba

# ARG token

# Add Maintainer Info
LABEL maintainer="rubenski@gmail.com"
LABEL application=oba-portal

# Install basic utilities
RUN apk add --no-cache bash
# RUN addgroup -g 1000 app && adduser -u 1000 -G app -D app

# RUN mkdir /app && mkdir /app/logs && chown -R app:app /app && chmod -R 755 /app && cd /app

# USER app

# The application's jar file
ARG JAR_FILE=target/oba-portal-*-SNAPSHOT.jar

# Add the application's jar to the container
ADD ${JAR_FILE} /app/oba-portal.jar


# Make port 8083 available to the world outside this container
EXPOSE 8083

# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/oba-portal.jar"]
