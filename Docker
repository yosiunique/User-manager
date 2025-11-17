
FROM amazoncorretto:21-alpine
ARG JAR_FILE=target/*.jar
RUN mkdir -p /home/loanRepayment/logs && chmod -R 777 /home/loanRepayment/logs
COPY ${JAR_FILE} app.jar
ENV SPRING_PROFILES_ACTIVE develop
ENV PORT 8080
ENV TZ=UTC
EXPOSE $PORT
CMD [  "java","-jar","app.jar","-Dserver.port=${PORT}" , "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}" ]