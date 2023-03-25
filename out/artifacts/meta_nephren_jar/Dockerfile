FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=./meta-nephren.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]