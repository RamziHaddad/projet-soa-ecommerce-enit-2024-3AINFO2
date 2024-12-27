FROM openjdk:17
EXPOSE 8080
ADD target/pricing-service.jar pricing-service.jar
ENTRYPOINT [ "java", "jar" , "/pricing-service.jar" ]