FROM openjdk:8-jre
ENV TZ=Europe/Madrid
WORKDIR /app
ADD /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]
