FROM openjdk:17-oracle
VOLUME /tmp
COPY target/*.war app.war
ENTRYPOINT ["java","-jar","/app.war"]
