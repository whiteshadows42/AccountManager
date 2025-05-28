FROM amazoncorretto:17.0.7-alpine
ADD target/accountmanager.jar accountmanager.jar
ENTRYPOINT ["java", "-jar", "accountmanager.jar"]