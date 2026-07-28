FROM azul-zulu:25-jdk
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]