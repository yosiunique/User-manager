FROM eclipse-temurin:25-jre-alpine-3.23
MAINTAINER Fentahun Amare and Yoseph Getachew <getachewy307@gmail.com>
ENV TZ=Africa/Addis_Ababa
# Copy your project files and build the jar (replacing your Jenkins steps)
COPY pom.xml .
COPY src ./src
RUN mvn -version && mvn clean install -DskipTests
ARG JAR_FILE=target/*.jar
RUN mkdir -p /home/loanRepayment/logs && chmod -R 777 /home/loanRepayment/logs

WORKDIR /home/spring
COPY ${JAR_FILE} app.jar
ENV SPRING_PROFILES_ACTIVE=develop
HEALTHCHECK CMD curl http://localhost:8080 --interval=30s --timeout=30s
ENV PORT=8080
EXPOSE $PORT
CMD [  "java","-XX:+UseContainerSupport","-XX:MaxRAMPercentage=75.0","-XX:InitialRAMPercentage=50.0","-XX:+UseG1GC","-XX:MaxGCPauseMillis=200","-XX:+UseStringDeduplication","-XX:+OptimizeStringConcat","-Djava.security.egd=file:/dev/./urandom","-jar","-Djasypt.encryptor.password=enat@1234","app.jar","-Dserver.port=${PORT}" , "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}" ]
