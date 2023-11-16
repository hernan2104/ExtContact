FROM amazoncorretto:17-alpine-jdk
MAINTAINER ITX
COPY target/ExtContact-0.0.1v1-SNAPSHOT.jar extcontactv1.jar
ENTRYPOINT ["java", "-jar", "/extcontactv1.jar"]