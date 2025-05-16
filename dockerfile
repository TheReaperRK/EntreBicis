FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY *.jar TutorialSpringBootPart4.jar
ENTRYPOINT ["java","-jar","/TutorialSpringBootPart4.jar"]
