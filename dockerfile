FROM openjdk:8-jre-alpine

EXPOSE 8080

USER 1001

CMD ["java",  "-jar", "/telescreen.jar"]

COPY ./telescreen/target/telescreen*.jar /telescreen.jar