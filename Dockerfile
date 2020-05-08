FROM openjdk:8-jdk-alpine
EXPOSE 8443 443 8080 8900 8901
VOLUME /tmp
ADD build/libs/config-service-*.jar app.jar
ENTRYPOINT exec java -Djava.security.egd=file:/dev/./urandom -jar /app.jar