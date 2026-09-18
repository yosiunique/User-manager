FROM maven:3.9-amazoncorretto-21-al2023 AS builder

WORKDIR /build

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests


FROM amazoncorretto:21-al2023

LABEL maintainer="Fentahun Amare and Yoseph Getachew <getachewy307@gmail.com>"

ENV TZ=Africa/Addis_Ababa
ENV SPRING_PROFILES_ACTIVE=develop
ENV PORT=8080

WORKDIR /home/spring

RUN mkdir -p /home/loanRepayment/logs \
    && chmod -R 777 /home/loanRepayment/logs

COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java"]

CMD ["-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-XX:InitialRAMPercentage=50.0", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=200", "-XX:+UseStringDeduplication", "-Djava.security.egd=file:/dev/./urandom", "-Djasypt.encryptor.password=enat@1234", "-Dserver.port=8080", "-Dspring.profiles.active=develop", "-jar", "app.jar"]

