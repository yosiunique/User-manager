# =========================
# 1. BUILD STAGE
# =========================
FROM maven:3.9-amazoncorretto-21-al2023 AS builder

WORKDIR /build

COPY pom.xml .
COPY src ./src

RUN mvn -version \
    && mvn clean package -DskipTests


# =========================
# 2. RUNTIME STAGE
# =========================
FROM amazoncorretto:21-al2023

LABEL maintainer="Fentahun Amare and Yoseph Getachew <getachewy307@gmail.com>"

ENV TZ=Africa/Addis_Ababa
ENV SPRING_PROFILES_ACTIVE=develop
ENV PORT=8080

WORKDIR /home/spring

# Create application log directory
RUN mkdir -p /home/loanRepayment/logs \
    && chmod -R 777 /home/loanRepayment/logs

# Copy the JAR produced by the builder stage
COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s \
            --timeout=10s \
            --start-period=60s \
            --retries=3 \
            CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java"]

CMD [
    "-XX:+UseContainerSupport",
    "-XX:MaxRAMPercentage=75.0",
    "-XX:InitialRAMPercentage=50.0",
    "-XX:+UseG1GC",
    "-XX:MaxGCPauseMillis=200",
    "-XX:+UseStringDeduplication",
    "-Djava.security.egd=file:/dev/./urandom",
    "-Djasypt.encryptor.password=enat@1234",
    "-Dserver.port=8080",
    "-Dspring.profiles.active=develop",
    "-jar",
    "app.jar"
]
