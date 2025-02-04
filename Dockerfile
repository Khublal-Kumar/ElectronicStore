FROM openjdk:21
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} E_Shop.jar
ENTRYPOINT ["java","-jar","/E_Shop.jar"]